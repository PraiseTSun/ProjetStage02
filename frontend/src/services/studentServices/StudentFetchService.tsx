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

export const putUploadStudentCV = (studentId: string, cv: number[], token: string): Promise<Response> => {
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

export const putStatusCv = (studentId: String, token: string): Promise<Response> => {
    return fetch(`http://localhost:8080/getStatutValidationCV/${studentId}`, {
        method: "PUT",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({
            "studentId": studentId,
            "token": token
        })
    });
}

export const putStudentContracts = (studentId: string, session: string, token: string): Promise<Response> => {
    return fetch(`http://localhost:8080/studentContracts/${studentId}_${session}`, {
        method: "PUT",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({
            token: token
        })
    });
}

export const putStudentSignatureContract =
    (studentId: string, contractId: string, signature: string, token: string): Promise<Response> => {
        return fetch("http://localhost:8080/studentSignatureContract", {
            method: "PUT",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify({
                userId: studentId,
                contractId: contractId,
                signature: signature,
                token: token
            })
        });
    }

export const putGetStudentInterviews = (studentId: string, token: string): Promise<Response> => {
    return fetch(`http://localhost:8080/getStudentInterviews/${studentId}`, {
        method: "PUT",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({"token": token})
    });
}

export const putStudentSelectDate = (studentId: string,
                                     interviewId: string,
                                     selectedDate: string,
                                     token: string): Promise<Response> => {
    return fetch("http://localhost:8080/studentSelectDate", {
        method: "PUT",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            studentId: studentId,
            interviewId: interviewId,
            selectedDate: selectedDate,
            token: token
        })
    });
}

export const removeStudentApplication = (token: string,applicationId:number, studentId: number): Promise<Response> => {
    return fetch("http://localhost:8080/removeStudentApplication", {
        method: "PUT",
        headers:{
            'Accept': 'application/json',
            'Content-Type':'application/json',
        },
        body: JSON.stringify({
            token: token,
            applicationId: applicationId,
            studentId: studentId
        })
    })
}

