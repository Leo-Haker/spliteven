import { styles } from "../utils/styles.js"

function SelectDropdown({items, value, onChange, placeholder, getLabel, getValue}){
    return (
        <select
            value={value}
            onChange={(e) => onChange(e.target.value)}
            className={styles.input}
            required
        >
            <option value="" disabled>{placeholder}</option>
            {items.map((item) => (
                <option key={getValue(item)} value={getValue(item)}>{getLabel(item)}</option>
            ))}

        </select>

    )
}

export default SelectDropdown