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
                <option value="Totalement en accord">Totalement en accord</option>
                <option value="Plutôt en accord">Plutôt en accord</option>
                <option value="Plutôt en désaccord">Plutôt en désaccord</option>
                <option value="Totalement en désaccord">Totalement en désaccord</option>
                <option value="N/A">N/A</option>
            </Form.Select>
        </Form.Group>
    );
}

export default RatingComponent;