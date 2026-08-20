import { styles } from "../utils/styles"

function CreateUserForm({ name, setName, email, setEmail,password, setPassword, onSubmit }) {
  return (
    <form onSubmit={onSubmit} className="flex flex-col gap-2 -mt-1">
      <input
        type="text"
        placeholder="Namn"
        value={name}
        onChange={(e) => setName(e.target.value)}
        className={styles.input}
        required
      />
      <input
        type="email"
        placeholder="E-postadress"
        value={email}
        onChange={(e) => setEmail(e.target.value)}
        className={styles.input}
        required
      />
      <input
      type="password"
      placeholder="Lösenord"
      value={password}
      onChange={(e) => setPassword(e.target.value)}
      className={styles.input}
      required
      >
      </input>
      <button
        type="submit"
        className={styles.buttonPrimary}
      >
        Bekräfta
      </button>
    </form>
  )
}

export default CreateUserForm