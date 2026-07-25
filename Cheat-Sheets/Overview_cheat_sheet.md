# Fullstack Cheat Sheet – koppla ihop Frontend, Backend & Databas

## Hela kedjan, översikt

```
React (localhost:5173)
   │  fetch('http://localhost:8080/api/expenses')
   ▼
Spring Boot / Tomcat (localhost:8080)
   │
   ▼
Controller  (@RestController)   -- tar emot HTTP-anropet
   │
   ▼
Service     (valfritt lager)    -- affärslogik
   │
   ▼
Repository  (JpaRepository)     -- pratar med databasen
   │
   ▼
PostgreSQL  (localhost:5432)
```

Frontend och backend är **två helt separata program** som pratar med varandra över HTTP – de körs på olika portar och vet inget om varandras kod, bara om det avtalade API:et (URL:er + JSON-format).

---

## Controller – tar emot requests

```java
@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseRepository expenseRepository;

    public ExpenseController(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    @GetMapping
    public List<Expense> getAll() {
        return expenseRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Expense> getOne(@PathVariable Long id) {
        return expenseRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Expense create(@Valid @RequestBody Expense expense) {
        return expenseRepository.save(expense);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        expenseRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
```

| Annotering | Betyder |
|---|---|
| `@RestController` | Klassen returnerar JSON direkt (inte HTML-vyer) |
| `@RequestMapping("/api/expenses")` | Bas-URL för alla metoder i klassen |
| `@GetMapping` | Hanterar GET-requests (hämta data) |
| `@PostMapping` | Hanterar POST-requests (skapa data) |
| `@PutMapping` | Hanterar PUT-requests (uppdatera data) |
| `@DeleteMapping` | Hanterar DELETE-requests (ta bort data) |
| `@PathVariable` | Hämtar en variabel från URL:en, t.ex. `/expenses/5` → `id=5` |
| `@RequestParam` | Hämtar en query-parameter, t.ex. `?month=2026-07` |
| `@RequestBody` | Tolkar JSON i request-kroppen som ett Java-objekt |
| `ResponseEntity<T>` | Ger full kontroll över statuskod + body i svaret |

---

## HTTP-metoder & statuskoder

| Metod | Används för | Vanlig statuskod vid lyckat svar |
|---|---|---|
| `GET` | Hämta data | `200 OK` |
| `POST` | Skapa ny data | `201 Created` (eller `200 OK`) |
| `PUT` | Ersätta befintlig data | `200 OK` |
| `PATCH` | Delvis uppdatera data | `200 OK` |
| `DELETE` | Ta bort data | `204 No Content` |

| Statuskod | Betyder |
|---|---|
| `200` | OK |
| `201` | Skapad |
| `204` | OK, inget innehåll att returnera |
| `400` | Felaktig request (t.ex. validering misslyckades) |
| `404` | Hittades inte |
| `500` | Serverfel |

---

## CORS – varför frontend inte kan nå backend direkt

Webbläsare blockerar som standard requests mellan olika **origins** (kombination av protokoll + domän + port). `localhost:5173` (React) och `localhost:8080` (Spring Boot) räknas som **olika origins**, trots att båda kör lokalt – du får ett CORS-fel i webbläsarens konsol om du inte tillåter det explicit.

**Lösning 1: Per controller**
```java
@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class ExpenseController { ... }
```

**Lösning 2: Global konfiguration (bättre för hela projektet)**
```java
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:5173")
                .allowedMethods("GET", "POST", "PUT", "DELETE");
    }
}
```

---

## Anropa backend från React

```jsx
const API_BASE = "http://localhost:8080/api";

// GET
async function loadExpenses() {
  const res = await fetch(`${API_BASE}/expenses`);
  if (!res.ok) throw new Error("Kunde inte hämta utgifter");
  return res.json();
}

// POST
async function createExpense(expense) {
  const res = await fetch(`${API_BASE}/expenses`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(expense),
  });
  if (!res.ok) throw new Error("Kunde inte spara utgift");
  return res.json();
}
```

**Bra vana: lägg bas-URL i en `.env`-fil istället för hårdkodad**
```
# frontend/.env
VITE_API_URL=http://localhost:8080/api
```
```jsx
const API_BASE = import.meta.env.VITE_API_URL;
```

---

## Entity vs DTO – vad skickas egentligen som JSON?

Att returnera en `@Entity` direkt från en controller funkar, men har fallgropar:

| Problem | Varför |
|---|---|
| Lazy-loading-fel | Om en relation är `LAZY` och Hibernate-sessionen stängts innan JSON skrivs, kastas ett fel |
| Oönskad data läcker ut | T.ex. lösenordsfält, interna ID:n, hela relationsträd |
| Oändlig loop | Om `Account` har `List<Person>` och `Person` har `List<Account>` kan JSON-serialiseringen gå i cirklar |

**Lösning: använd ett separat DTO (Data Transfer Object)**
```java
public record ExpenseDto(Long id, String description, BigDecimal amount, String paidByName) {
}

@GetMapping
public List<ExpenseDto> getAll() {
    return expenseRepository.findAll().stream()
            .map(e -> new ExpenseDto(e.getId(), e.getDescription(), e.getAmount(), e.getPaidBy().getName()))
            .toList();
}
```
DTO:n bestämmer exakt vad som skickas till frontend – inget mer, inget mindre.

---

## Felsökningsflöde när "det inte funkar"

1. **Fungerar backend-anropet isolerat?** Testa i terminalen:
   ```bash
   curl http://localhost:8080/api/expenses
   ```
2. **CORS-fel i webbläsarens konsol (F12)?** → kolla CORS-konfigurationen ovan
3. **404 Not Found?** → kolla att URL:en i `fetch()` matchar `@RequestMapping` + `@GetMapping` exakt
4. **500 Internal Server Error?** → kolla backend-terminalen för stacktrace, ofta ett databas- eller null-relaterat fel
5. **JSON ser konstigt ut / saknar fält?** → kolla om du returnerar en `@Entity` direkt (se DTO-avsnittet ovan)