import { styles } from "../utils/styles"

function CreateAccountForm({ name, setName, onSubmit }) {
  return (
    <form onSubmit={onSubmit} className="flex flex-col gap-2 -mt-1">
      <input
        type="text"
        placeholder="Kontots namn"
        value={name}
        onChange={(e) => setName(e.target.value)}
        className={styles.input}
        required
      />
      <button
        type="submit"
        className={styles.buttonPrimary}
      >
        Bekräfta
      </button>
    </form>
  )
}

export default CreateAccountForm