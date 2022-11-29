import {generateAlert} from "./UniversalUtilService";

export const putConfirmEmail = (userType: string, id: string): Promise<Response> => {
    return fetch("http://localhost:8080/confirmEmail/" + userType + "/" + id, {
        method: "PUT",
        headers: {"Content-Type": "application/json"},
    });
}

export const putUserType = (userType: string, token: string): Promise<Response> => {
    return fetch("http://localhost:8080/" + userType, {
        method: "PUT",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({"token": token})
    })
}

export const postUserType = (userType: string, user: object): Promise<Response> => {
    return fetch("http://localhost:8080/create" + userType, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(user)
    })
}
export const postUserTypeLogin = (userType: string, email: string, password: string): Promise<Response> => {
    return fetch("http://localhost:8080/" + userType + "/login", {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({"email": email, "password": password})
    })
}

export const postReport = (problem: { problemCategory: string, problemDetails: string }): Promise<Response> => {
    return fetch("http://localhost:8080/reportProblem", {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify(problem)
    })
}
export const universalFetch = (fetchFunction: Function, ifResponseOk: Function): void => {
    fetchFunction().then(async (response: Response) => {
        if (response.ok) {
            await ifResponseOk(response);
        } else {
            generateAlert();
        }
    }).catch(() => {
        generateAlert();
    })
}