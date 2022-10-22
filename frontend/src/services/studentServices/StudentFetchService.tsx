export const putGetOffers = (studentId: string, token: string): Promise<Response> => {
    return fetch("http://localhost:8080/getOffers/" + studentId, {
        method: "PUT",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({"token": token})
    });
}

export const putStudentApplys = (studentId: string, token: string): Promise<Response> => {
    return fetch("http://localhost:8080/studentApplys/" + studentId, {
        method: "PUT",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({"token": token})
    });
}

export const putApplyToOffer = (studentId: string, offerId: string, token: string): Promise<Response> => {
    return fetch("http://localhost:8080/applyToOffer/" + studentId + "_" + offerId, {
        method: "PUT",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({"token": token})
    });
}

export const putGetOfferStudent = (offerId: string, token: string): Promise<Response> => {
    return fetch("http://localhost:8080/getOfferStudent/" + offerId, {
        method: "PUT",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({"token": token})
    });
}

export const putUploadStudentCV = (studentId: String, cv: number[], token: string): Promise<Response> => {
    return fetch("http://localhost:8080/uploadStudentCV", {
        method: "PUT",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({
            studentId: studentId,
            pdf: cv,
            token: token
        })
    });
}