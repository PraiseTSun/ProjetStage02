export default interface IUser {
    id: string,
    token: string,
    firstName: string,
    lastName: string,
    cv: string | null,
    userType: string
}