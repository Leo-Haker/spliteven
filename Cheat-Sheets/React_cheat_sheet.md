# React Cheat Sheet

## Komponenter

```jsx
function Greeting({ name }) {
  return <h1>Hej {name}!</h1>;
}

export default Greeting;
```

| Begrepp | Betyder |
|---|---|
| Funktionskomponent | Vanlig JS-funktion som returnerar JSX |
| Props | Argument som skickas in till komponenten (`{ name }`) |
| JSX | HTML-liknande syntax inuti JavaScript |
| `export default` | Gör komponenten importerbar från andra filer |

```jsx
import Greeting from './Greeting';
<Greeting name="Leo" />
```

---

## useState (lokalt state)

```jsx
import { useState } from 'react';

function Counter() {
  const [count, setCount] = useState(0);

  return (
    <button onClick={() => setCount(count + 1)}>
      Klickat {count} gånger
    </button>
  );
}
```

| Del | Betyder |
|---|---|
| `useState(0)` | Startvärde för state |
| `count` | Aktuellt värde |
| `setCount(x)` | Funktion för att uppdatera värdet (triggar omrendering) |
| `setCount(prev => prev + 1)` | Uppdatering baserad på föregående värde (säkrare i loopar/callbacks) |

---

## useEffect (sidoeffekter)

```jsx
import { useEffect, useState } from 'react';

function Expenses() {
  const [expenses, setExpenses] = useState([]);

  useEffect(() => {
    fetch('/api/expenses')
      .then(res => res.json())
      .then(data => setExpenses(data));
  }, []); // tom array = körs bara vid första renderingen

  return <ul>{expenses.map(e => <li key={e.id}>{e.description}</li>)}</ul>;
}
```

| Beroende-array | Körs |
|---|---|
| `[]` | Bara en gång, direkt efter första renderingen |
| `[value]` | Vid första renderingen + varje gång `value` ändras |
| (ingen array) | Vid **varje** rendering (ovanligt, oftast fel) |

---

## Lista med `.map()`

```jsx
{expenses.map(expense => (
  <li key={expense.id}>{expense.description} - {expense.amount} kr</li>
))}
```
⚠️ `key` krävs på varje element i en lista – använd ett unikt id, aldrig index om listan kan ändra ordning.

---

## Villkorlig rendering

```jsx
{isLoading && <p>Laddar...</p>}
{error ? <p>Fel: {error}</p> : <p>Allt ok</p>}
{user != null && <p>Inloggad som {user.name}</p>}
```

---

## Formulär & events

```jsx
function ExpenseForm() {
  const [amount, setAmount] = useState('');

  function handleSubmit(e) {
    e.preventDefault();       // hindra sidan från att laddas om
    console.log(amount);
  }

  return (
    <form onSubmit={handleSubmit}>
      <input
        value={amount}
        onChange={e => setAmount(e.target.value)}
        type="number"
      />
      <button type="submit">Spara</button>
    </form>
  );
}
```

| Event | När den triggas |
|---|---|
| `onClick` | Vid klick |
| `onChange` | Värdet i ett fält ändras |
| `onSubmit` | Ett formulär skickas |
| `e.preventDefault()` | Stoppar webbläsarens standardbeteende |
| `e.target.value` | Aktuellt värde i fältet som triggade eventet |

---

## fetch mot backend

```jsx
// GET
fetch('http://localhost:8080/api/expenses')
  .then(res => res.json())
  .then(data => console.log(data));

// POST
fetch('http://localhost:8080/api/expenses', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({ description: 'Mat', amount: 100 }),
})
  .then(res => res.json())
  .then(data => console.log(data));
```

**Med async/await (ofta lättare att läsa):**
```jsx
async function loadExpenses() {
  const res = await fetch('/api/expenses');
  const data = await res.json();
  setExpenses(data);
}
```

---

## Props vs State

| | Props | State |
|---|---|---|
| Vem äger datan | Föräldern, skickas ned | Komponenten själv |
| Går att ändra i komponenten | Nej (read-only) | Ja, via `setX()` |
| Exempel | `<Greeting name="Leo" />` | `const [count, setCount] = useState(0)` |

---

## Vanliga hooks

| Hook | Används för |
|---|---|
| `useState` | Lokalt state i en komponent |
| `useEffect` | Sidoeffekter (fetch, timers, prenumerationer) |
| `useContext` | Läsa delat state utan att skicka props genom flera nivåer |
| `useRef` | Referens till ett DOM-element eller värde som inte ska trigga omrendering |
| `useMemo` | Cachar ett beräknat värde tills dess beroenden ändras |

---

## Vanliga fallgropar

- **Glömmer `key` i listor** → React klagar i konsolen, kan ge konstiga buggar vid omrendering
- **Muterar state direkt** (`state.push(x)`) istället för att skapa en ny array/objekt → React märker inte ändringen
- **Tom beroende-array i `useEffect` men använder state inuti** → "stale closure", får gamla värden
- **Glömmer `e.preventDefault()`** i formulär → sidan laddas om vid submit