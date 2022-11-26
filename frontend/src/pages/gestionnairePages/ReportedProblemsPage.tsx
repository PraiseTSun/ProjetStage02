import PageHeader from "../../components/universalComponents/PageHeader";
import ReportedProblems from "../../components/gestionnaireComponents/ReportedProblems";
import IUser from "../../models/IUser";
import {Container} from "react-bootstrap";

const ReportedProblemsPage = ({connectedUser: connectedUser}: { connectedUser: IUser }) => {
    return (
        <Container className="min-vh-100">
            <PageHeader title={"Problèmes signalés"}/>
            <ReportedProblems user={connectedUser}/>
        </Container>)
}
export default ReportedProblemsPage