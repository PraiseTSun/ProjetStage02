import {Form} from "react-bootstrap";

const FormFeedBackInvalid = (): JSX.Element => {
    return (
        <Form.Control.Feedback className="fw-bold" type="invalid">Vous devez remplir ce champs!</Form.Control.Feedback>
    );
}

export default FormFeedBackInvalid;