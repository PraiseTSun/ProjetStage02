import {render, screen, fireEvent} from "@testing-library/react";
import IUser from "./models/IUser";
import UploaderMonCV from "./pages/UploaderMonCV";

const etudiant: IUser = {
    id : 1,
    token : "34245",
    firstName: "Yan",
    lastName: "Zhou",
    userType: "student"
}

const uploaderCV = (cv : File) => {
    const inputUploaderMonCV = screen.getByTestId("uploaderMonCV")
    fireEvent.change(inputUploaderMonCV , {target:{value:cv.name}})
}
describe('App', () => {

    it('test page uploader mon cv ', async () => {

            render(<UploaderMonCV user={etudiant}/>);
            const cv = new File([""],"test.pdf");
            uploaderCV(cv);
            const h4Element = screen.getByRole("heading", {name: /Document CV/i});
            const buttonElement = screen.getByRole("button", {name: /Envoyer/i});

            expect(h4Element).toBeInTheDocument();
            expect(buttonElement).toBeInTheDocument();

    });
});