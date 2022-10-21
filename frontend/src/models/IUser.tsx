export default interface IUser {
    id: string,
    token: string,
    firstName: string,
    lastName: string,
    companyName: string | null,
    cv: string | null,
    userType: string
}