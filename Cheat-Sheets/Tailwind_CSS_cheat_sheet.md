# Tailwind CSS Cheat Sheet

## Grundprincip

Istället för att skriva egen CSS, kombinerar du färdiga utility-klasser direkt i HTML/JSX:

```jsx
<button className="bg-slate-900 text-white px-4 py-2 rounded-lg hover:bg-slate-700">
  Spara
</button>
```

---

## Spacing (padding & margin)

| Klass | Betyder |
|---|---|
| `p-4` | Padding på alla sidor (4 = 1rem = 16px) |
| `px-4` / `py-4` | Padding horisontellt / vertikalt |
| `pt-4` `pr-4` `pb-4` `pl-4` | Padding top/right/bottom/left |
| `m-4` | Margin på alla sidor |
| `mx-auto` | Horisontell centrering (kräver fast bredd) |
| `gap-4` | Mellanrum mellan flex/grid-barn |

Skalan: `0, 1, 2, 3, 4, 5, 6, 8, 10, 12, 16, 20, 24...` (1 enhet ≈ 4px)

---

## Layout: Flexbox

```jsx
<div className="flex items-center justify-between gap-4">
```

| Klass | Betyder |
|---|---|
| `flex` | Aktiverar flexbox |
| `flex-col` | Kolumn istället för rad |
| `items-center` | Centrerar barn vertikalt (i en rad) |
| `justify-center` | Centrerar barn horisontellt |
| `justify-between` | Sprider ut barn med mellanrum |
| `flex-1` | Tar upp allt tillgängligt utrymme |
| `flex-wrap` | Tillåter radbrytning av barn |

---

## Layout: Grid

```jsx
<div className="grid grid-cols-3 gap-4">
```

| Klass | Betyder |
|---|---|
| `grid` | Aktiverar grid |
| `grid-cols-3` | 3 kolumner |
| `col-span-2` | Elementet tar upp 2 kolumner |

---

## Storlek

| Klass | Betyder |
|---|---|
| `w-full` | Bredd 100% |
| `w-1/2` | Bredd 50% |
| `w-64` | Fast bredd (64 = 16rem = 256px) |
| `h-screen` | Höjd = hela skärmen |
| `min-h-screen` | Minst hela skärmens höjd |
| `max-w-sm` / `max-w-md` / `max-w-lg` | Maxbredd (bra för kort/formulär) |

---

## Färger

```jsx
className="bg-slate-900 text-white border-slate-200"
```

Mönster: `{egenskap}-{färg}-{nyans}`
- `bg-` = bakgrund, `text-` = textfärg, `border-` = kantfärg
- Nyans går från `50` (ljusast) till `950` (mörkast)
- Vanliga färgnamn: `slate`, `gray`, `red`, `green`, `blue`, `yellow`, `indigo`

---

## Typografi

| Klass | Betyder |
|---|---|
| `text-sm` / `text-base` / `text-lg` / `text-xl` / `text-2xl` | Textstorlek |
| `font-normal` / `font-medium` / `font-semibold` / `font-bold` | Vikt |
| `text-center` / `text-left` / `text-right` | Justering |
| `truncate` | Klipper av text med "..." om den inte får plats |

---

## Border & rundade hörn

| Klass | Betyder |
|---|---|
| `border` | Tunn kant runt hela elementet |
| `border-2` | Tjockare kant |
| `rounded` | Lite rundade hörn |
| `rounded-lg` / `rounded-xl` / `rounded-2xl` | Mer rundade hörn |
| `rounded-full` | Helt runt (cirkel om kvadratisk) |

---

## Skuggor & effekter

| Klass | Betyder |
|---|---|
| `shadow-sm` / `shadow-md` / `shadow-lg` | Skugga, ökande storlek |
| `opacity-50` | 50% genomskinlighet |
| `transition-colors` | Mjuk övergång vid färgändring (t.ex. hover) |

---

## Hover, focus & tillstånd

```jsx
className="bg-slate-900 hover:bg-slate-700 focus:ring-2 disabled:opacity-50"
```

| Prefix | Gäller när |
|---|---|
| `hover:` | Musen är över elementet |
| `focus:` | Elementet är fokuserat (t.ex. ett input-fält) |
| `disabled:` | Elementet är inaktiverat |
| `active:` | Elementet klickas ned |

---

## Responsiv design

```jsx
className="w-full md:w-1/2 lg:w-1/3"
```

| Prefix | Gäller från bredd |
|---|---|
| `sm:` | ≥ 640px |
| `md:` | ≥ 768px |
| `lg:` | ≥ 1024px |
| `xl:` | ≥ 1280px |

Mobile-first: klasser utan prefix gäller alltid, prefix-klasser "tar över" från den bredden och uppåt.

---

## Vanliga fallgropar

- **Dynamiskt hopsatta klassnamn** (`` `bg-${color}-500` ``) → Tailwind ser inte klassen vid byggtillfället och den försvinner. Skriv hela klassnamnet ut, t.ex. med en mapping.
- **Glömmer `min-h-screen` på rot-elementet** → bakgrundsfärg fyller inte hela skärmen om innehållet är kort
- **`mx-auto` utan en satt bredd** (t.ex. `max-w-sm`) → gör ingenting, centrering kräver en begränsad bredd