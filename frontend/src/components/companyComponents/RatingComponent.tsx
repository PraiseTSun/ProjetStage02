import {Form} from "react-bootstrap";

const RatingComponent = ({
                             label,
                             value,
                             setValue
                         }: {
    label: string, value: any, setValue: Function,
}): JSX.Element => {
    return (
        <Form.Group className="mt-2">
            <Form.Label>{label}</Form.Label>
            <Form.Select value={value} required onChange={event => setValue(event.target.value)}>
                <option value="" hidden disabled>Veuillez choisir une option</option>
                <option value="totalementEnAccord">Totalement en accord</option>
                <option value="plutotEnAccord">Plutôt en accord</option>
                <option value="plutotEnDesaccord">Plutôt en désaccord</option>
                <option value="totalementEnDesaccord">Totalement en désaccord</option>
                <option value="impossibleDeSePronnoncer">N/A</option>
            </Form.Select>
        </Form.Group>
    );
}

export default RatingComponent;