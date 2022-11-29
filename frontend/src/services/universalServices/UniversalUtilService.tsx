import IUser from "../../models/IUser";

export const generateAlert = () => {
    alert("Une erreur est survenue, réssayez.");
    window.location.href = "/"
}
export const hasCv = (connectedUser: IUser) =>
    connectedUser.cv !== null && connectedUser.cv !== undefined && connectedUser.cv !== "" && connectedUser.cv !== "null" && connectedUser.cv !== "[]"
export const hasCvToValidate = (connectedUser: IUser) =>
    connectedUser.cvToValidate !== null && connectedUser.cvToValidate !== undefined && connectedUser.cvToValidate !== "" && connectedUser.cvToValidate !== "null" && connectedUser.cvToValidate !== "[]"

