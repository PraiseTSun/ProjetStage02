import {act, fireEvent, render, screen} from "@testing-library/react";
import LoginPage from "../LoginPage";
import * as React from "react";

describe("LoginPageTests", () => {
    beforeEach(async () => {
        const setUser = jest.fn()

        await act(async () => {
            render(<LoginPage setUser={setUser}/>)
        })
    });


    it("HeaderIsPresentTest", () => {
        const headerElement = screen.getByText(/OSE KILLER/i);
        expect(headerElement).toBeInTheDocument();
    })

    it("RegisterFormTextTest", async () => {
        const linkElement = await screen.findByText(/Nouveau utilisateur\? Inscrivez vous ici./i);

        await act(async () => {
            fireEvent.click(linkElement);
        })

        const elementEntreprise = await screen.getByTestId(/entrepriseInscriptionForm/i);
        const elementEtudiant = await screen.getByText(/Etudiant/i);
        expect(elementEntreprise).toBeInTheDocument();
        expect(elementEtudiant).toBeInTheDocument();

    })

    it("CompanyFormErrorTest", async () => {
        const linkElement = await screen.findByText(/Nouveau utilisateur\? Inscrivez vous ici./i);
        fireEvent.click(linkElement)
        const elementEntreprise = await screen.getByTestId(/entrepriseInscriptionForm/i)
        fireEvent.click(elementEntreprise)

        //dans la page formulaireEntreprise
        const inputNomElement = await screen.findByTestId("nomInputFormulaireEntreprise")
        const labelNomElement = await screen.findByTestId("nomLabelFormulaireEntreprise")
        const inputPrenomElement = await screen.findByTestId("prenomInputFormulaireEntreprise")
        const labelPrenomElement = await screen.findByTestId("prenomLabelFormulaireEntreprise")
        const inputEntrepriseElement = await screen.findByTestId("inputEntrepriseFormulaireEntreprise")
        const labelEntrepriseElement = await screen.findByTestId("labelEntrepriseFormulaireEntreprise")
        const inputCourrielElement = await screen.findByTestId("inputCourrielFormulaireEntreprise")
        const labelCourrielElement = await screen.findByTestId("labelCourrielFormulaireEntreprise")
        const inputMotDePasseElement = await screen.findByTestId("inputCourrielFormulaireEntreprise")
        const labelMotDePasseElement = await screen.findByTestId("labelCourrielFormulaireEntreprise")
        const inputVerifierMotDePasseElement = await screen.findByTestId("inputCourrielFormulaireEntreprise")
        const labelVerifierMotDePasseElement = await screen.findByTestId("labelCourrielFormulaireEntreprise")
        const labelDepartementElement = await screen.findByTestId("labelDepartementFormulaireEntreprise")
        const selectDepartementElement = await screen.findByTestId("selelctDepartementFormulaireEntreprise")
        const optionsDepartementElement = await screen.getAllByTestId("selelct-option-FormulaireEntreprise")
        const buttonElement = await screen.getByTestId("button_inscrire")

        await act(async () => {
            fireEvent.change(inputNomElement, {target: {value: "n"}})
            fireEvent.change(inputPrenomElement, {target: {value: "n"}})
            fireEvent.change(inputEntrepriseElement, {target: {value: "e"}})
            fireEvent.change(inputCourrielElement, {target: {value: "cour"}})
            fireEvent.change(inputMotDePasseElement, {target: {value: 12345}})
            fireEvent.change(inputVerifierMotDePasseElement, {target: {value: 12345678}})
        })

        const erreurPourNomElement = await screen.findByTestId("errorNomFormulaireEntreprise")
        const erreurPourPrenomElement = await screen.findByTestId("errorPrenomFormulaireEntreprise")
        const erreurPourEntrepriseElement = await screen.findByTestId("errorEntrepriseFormulaireEntreprise")
        const erreurPourCourrielElement = await screen.findByTestId("errorCourrielFormulaireEntreprise")
        const erreurPourMotDePasseElement = await screen.findByTestId("errorMotDePasseFormulaireEntreprise")

        expect(erreurPourNomElement).toBeInTheDocument()
        expect(labelNomElement).toBeInTheDocument()
        expect(erreurPourPrenomElement).toBeInTheDocument()
        expect(labelPrenomElement).toBeInTheDocument()
        expect(erreurPourEntrepriseElement).toBeInTheDocument()
        expect(labelEntrepriseElement).toBeInTheDocument()
        expect(erreurPourCourrielElement).toBeInTheDocument()
        expect(labelCourrielElement).toBeInTheDocument()
        expect(erreurPourMotDePasseElement).toBeInTheDocument()
        expect(labelMotDePasseElement).toBeInTheDocument()
        expect(labelVerifierMotDePasseElement).toBeInTheDocument()
        expect(labelDepartementElement).toBeInTheDocument()
        expect(selectDepartementElement).toBeInTheDocument()
        expect(optionsDepartementElement.length).toBe(3)
        expect(buttonElement).toBeInTheDocument()
    })

    it("StudentFormErrorTest", async () => {
        const linkElement = await screen.findByText(/Nouveau utilisateur\? Inscrivez vous ici./i)
        await act(async () => {
            fireEvent.click(linkElement)
        });

        const elementEtudiant = await screen.findByText(/Etudiant/i)
        await act(async () => {
            fireEvent.click(elementEtudiant)
        });
        
        //dans la page formulaireEtudiant
        const inputNomElement = await screen.findByTestId("intputNomFormulaireEtudiant")
        const labelNomElement = await screen.findByTestId("labelNomFormulaireEtudiant")
        const inputPrenomElement = await screen.findByTestId("inputPrenomFormulaireEtudiant")
        const labelPrenomElement = await screen.findByTestId("labelPrenomFormulaireEtudiant")
        const inputCourrielElement = await screen.findByTestId("inputCourrielFormulaireEtudiant")
        const labelCourrielElement = await screen.findByTestId("labelCourrielFormulaireEtudiant")
        const inputMotDePasseElement = await screen.findByTestId("inputMotDePasseFormulaireEtudiant")
        const labelMotDePasseElement = await screen.findByTestId("labelMotDePasseFormulaireEtudiant")
        const inputVerifierMotDePasseElement = await screen.findByTestId("inputVerifierMotDePasseFormulaireEtudiant")
        const labelVerifierMotDePasseElement = await screen.findByTestId("labelVerifierMotDePasseFormulaireEtudiant")
        const labelDepartementElement = await screen.findByTestId("labelDePartementFormulaireEtudiant")
        const selectDepartementElement = await screen.findByTestId("selelctDepartementFormulaireEntreprise")
        const optionsDepartementElement = await screen.getAllByTestId("select-option-DePartementFormulaireEtudiant")
        const buttonElement = await screen.getByTestId("button_inscrire_etudiant")

        await act(async () => {
            fireEvent.change(inputNomElement, {target: {value: "e"}})
            fireEvent.change(inputPrenomElement, {target: {value: "e"}})
            fireEvent.change(inputCourrielElement, {target: {value: "cour"}})
            fireEvent.change(inputMotDePasseElement, {target: {value: 12345}})
            fireEvent.change(inputVerifierMotDePasseElement, {target: {value: 12345678}})
        })

        const errorPourNomFormulaireEtudiant = await screen.findByTestId("errorNomFormulaireEtudiant")
        const erreurPourPrenomElement = await screen.findByTestId("errorPrenomFormulaireEtudiant")
        const erreurPourCourrielElement = await screen.findByTestId("errorCourrielFormulaireEtudiant")
        const erreurPourMotDePasseElement = await screen.findByTestId("errorMotDePasseFormulaireEtudiant")

        expect(errorPourNomFormulaireEtudiant).toBeInTheDocument()
        expect(labelNomElement).toBeInTheDocument()
        expect(erreurPourPrenomElement).toBeInTheDocument()
        expect(labelPrenomElement).toBeInTheDocument()
        expect(erreurPourCourrielElement).toBeInTheDocument()
        expect(labelCourrielElement).toBeInTheDocument()
        expect(erreurPourMotDePasseElement).toBeInTheDocument()
        expect(labelMotDePasseElement).toBeInTheDocument()
        expect(labelVerifierMotDePasseElement).toBeInTheDocument()
        expect(labelDepartementElement).toBeInTheDocument()
        expect(selectDepartementElement).toBeInTheDocument()
        expect(optionsDepartementElement.length).toBe(3)
        expect(buttonElement).toBeInTheDocument()
    })

    it("LoginFormTest", async () => {
        const tiggkeElementEtudiant = await screen.getByText(/Étudiant/i)
        const tiggkeElementEntreprise = await screen.getByText(/Entreprise/i)
        const tiggkeElementEtudiantGestionnaire = await screen.getByText(/Gestionnaire/i)
        const labelElementCourriel = await screen.getByText(/Adresse courriel/i)
        const inputElementCourriel = await screen.getByTestId("courreielLoginForm")
        const labelElementMotDePasse = await screen.getByTestId("labelmotDePasseLoginForm")
        const inputElementMotDePasse = await screen.getByTestId("motDePasseLoginForm")
        const buttonElement = await screen.getByRole("button", {name: /Connecter/i})

        expect(tiggkeElementEtudiant).toBeInTheDocument()
        expect(tiggkeElementEntreprise).toBeInTheDocument()
        expect(tiggkeElementEtudiantGestionnaire).toBeInTheDocument()
        expect(labelElementCourriel).toBeInTheDocument()
        expect(inputElementCourriel).toBeInTheDocument()
        expect(screen.getByText(/courriel invalide/i)).toBeInTheDocument()
        expect(labelElementMotDePasse).toBeInTheDocument()
        expect(inputElementMotDePasse).toBeInTheDocument()
        expect(screen.getByText(/Mot de passe plus petit que 8 caractères/i)).toBeInTheDocument()
        expect(buttonElement).toBeInTheDocument()
    })
})