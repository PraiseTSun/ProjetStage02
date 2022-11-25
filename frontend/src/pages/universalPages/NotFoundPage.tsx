import {Col, Container, Row} from "react-bootstrap";
import {Link} from "react-router-dom";

const NotFoundPage = (): JSX.Element => {
    return (
        <Container className="min-vh-100">
            <Row>
                <Col className="m-auto text-center text-white">
                    <h1 className="display-1">Cette page n'existe pas</h1>
                    <Link to="/" className="btn mt-5 btn-outline-primary text-white">Retour à la page principale</Link>
                </Col>
            </Row>
        </Container>
    )
}

export default NotFoundPage;