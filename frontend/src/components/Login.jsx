import { styles } from "../utils/styles"

function Login({ email, setEmail, password, setPassword, onSubmit }) {
  return (
    <form onSubmit={onSubmit} className="flex flex-col gap-2 -mt-1">
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
      />
      <button
        type="submit"
        className={styles.buttonPrimary}
      >
        Logga in
      </button>
    </form>
  )
}

export default Login