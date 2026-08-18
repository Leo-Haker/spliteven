# SQL Cheat Sheet (PostgreSQL)

## Skapa & ändra tabeller

```sql
CREATE TABLE person (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    created_at DATE DEFAULT CURRENT_DATE
);

CREATE TABLE expense (
    id BIGSERIAL PRIMARY KEY,
    account_id BIGINT NOT NULL REFERENCES account(id),
    amount NUMERIC(12,2) NOT NULL,
    date DATE NOT NULL
);
```

| Kommando | Vad det gör |
|---|---|
| `ALTER TABLE t ADD COLUMN c type` | Lägger till en kolumn |
| `ALTER TABLE t DROP COLUMN c` | Tar bort en kolumn |
| `ALTER TABLE t RENAME COLUMN a TO b` | Döper om en kolumn |
| `ALTER TABLE t RENAME TO nytt_namn` | Döper om hela tabellen |
| `DROP TABLE IF EXISTS t CASCADE` | Tar bort tabellen (och beroenden) |

---

## Datatyper

| Typ | Beskrivning |
|---|---|
| `BIGSERIAL` / `SERIAL` | Auto-inkrementerande heltal (64/32 bit) |
| `INT` / `BIGINT` | Heltal |
| `NUMERIC(p, s)` | Exakt decimaltal, `p` = totalt antal siffror, `s` = antal decimaler |
| `VARCHAR(n)` | Text, max `n` tecken |
| `TEXT` | Text utan längdgräns |
| `BOOLEAN` | `true` / `false` |
| `DATE` | Bara datum |
| `TIMESTAMP` | Datum + tid |

---

## Constraints

| Constraint | Betyder |
|---|---|
| `PRIMARY KEY` | Unikt identifierar varje rad |
| `REFERENCES tabell(id)` | Foreign key, länkar till en annan tabell |
| `NOT NULL` | Värde krävs |
| `UNIQUE` | Inga dubbletter tillåtna |
| `DEFAULT värde` | Standardvärde om inget anges |
| `CHECK (amount > 0)` | Egen valideringsregel |

---

## Läsa data (SELECT)

```sql
SELECT * FROM person;
SELECT name, email FROM person WHERE id = 1;
SELECT * FROM person WHERE name LIKE 'L%';          -- börjar med L
SELECT * FROM person ORDER BY name ASC LIMIT 10;
SELECT DISTINCT name FROM person;
SELECT * FROM person WHERE id IN (1, 2, 3);
SELECT * FROM person WHERE created_at BETWEEN '2026-01-01' AND '2026-12-31';
```

---

## Joins

```sql
-- INNER JOIN: bara rader som matchar i båda tabellerna
SELECT e.description, p.name
FROM expense e
JOIN person p ON e.paid_by_id = p.id;

-- LEFT JOIN: alla från vänster tabell, även utan matchning
SELECT p.name, e.description
FROM person p
LEFT JOIN expense e ON e.paid_by_id = p.id;
```

---

## Gruppering & aggregering

```sql
SELECT paid_by_id, SUM(amount) AS total
FROM expense
GROUP BY paid_by_id
HAVING SUM(amount) > 100;
```

| Funktion | Vad den gör |
|---|---|
| `SUM(x)` | Summa |
| `AVG(x)` | Medelvärde |
| `MIN(x)` / `MAX(x)` | Minsta/största värde |
| `COUNT(x)` | Antal rader |

---

## Ändra & ta bort data

```sql
UPDATE person SET email = 'new@mail.com' WHERE id = 1;
DELETE FROM person WHERE id = 1;
```
⚠️ `DELETE FROM person;` utan `WHERE` tar bort **alla** rader.
```sql
TRUNCATE TABLE membership_request, expense, account_person, account, person RESTART IDENTITY CASCADE;
```
`TRUNCATE` tömmer tabellerna helt (snabbare än `DELETE FROM` för stora mängder data), `RESTART IDENTITY` nollstället auto-increment-räknaren till 1 igen, `CASCADE` tar även bort beroende rader i andra tabeller (t.ex. `account_person`).


---

## Användbara psql-kommandon

| Kommando | Vad det gör |
|---|---|
| `\l` | Lista alla databaser |
| `\c dbname` | Byt till en databas |
| `\dt` | Lista tabeller i aktuell databas |
| `\d tablename` | Visa en tabells kolumner och typer |
| `\du` | Lista användare/roller |
| `\x` | Växla expanderad vy (lättare att läsa breda rader) |
| `\q` | Avsluta psql |

**Köra en SQL-fil från terminalen:**
```bash
psql -h localhost -U user -d dbname -f schema.sql
```

**Öppna SQL i terminalen:**
```bash
psql -h localhost -U user -d dbname 
```

---

## Vanliga fallgropar

- **Reserverade ord** som tabell-/kolumnnamn (`user`, `order`) kräver citattecken: `"user"`
- **`CASCADE`** vid `DROP TABLE` tar även bort beroende data – använd försiktigt
- **`NUMERIC(p,s)`**: `p` = totalt antal siffror, `s` = antal decimaler (inte "max-värde")
- **`NULL` med `=`** ger alltid `NULL`, aldrig `true`/`false` → använd `IS NULL` / `IS NOT NULL`