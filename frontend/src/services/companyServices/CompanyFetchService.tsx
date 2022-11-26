import IOffer from "../../models/IOffer";
import IStudentEvaluationFormFields from "../../models/IStudentEvaluationFormFields";

export const postCreateOffre = (offer: IOffer): Promise<Response> => {
    return fetch("http://localhost:8080/createOffre", {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify(offer)
    });
};


export const putcompanyContracts = (companyId: string, token: string, saison: string, year: number): Promise<Response> => {
    return fetch(`http://localhost:8080/companyContracts/${companyId}_${saison} ${year}`, {
        method: "PUT",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({token: token})
    })
}

export const putCompanySignatureContract = (token: string, companyId: string, contratId: number, signature: string): Promise<Response> => {

    return fetch(`http://localhost:8080/companySignatureContract`, {
        method: "PUT",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({token: token, userId: Number(companyId), contractId: contratId, signature: signature})
    })
}


export const putCompanyOffers = (companyId: string, token: string): Promise<Response> => {
    return fetch("http://localhost:8080/company/validatedOffers/" + companyId, {
        method: "PUT",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({"token": token})
    });
}

export const putStudentCv = (studentId: string, token: string): Promise<Response> => {
    return fetch("http://localhost:8080/company/studentCv/" + studentId, {
        method: "PUT",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({"token": token})
    });
}

export const putOfferApplications = (offerId: string, token: string): Promise<Response> => {
    return fetch("http://localhost:8080/offer/" + offerId + "/applications", {
        method: "PUT",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({"token": token})
    });
}

export const putStudentAcceptation = (offerId: string, studentId: string, token: string): Promise<Response> => {
    return fetch(`http://localhost:8080/studentAcceptation/${offerId}_${studentId}`, {
        method: "PUT",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({"token": token})
    });
}

export const putAcceptedStudentsForOffer = (offerId: string, token: string): Promise<Response> => {
    return fetch(`http://localhost:8080/getAcceptedStudentsForOffer/${offerId}`, {
        method: "PUT",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({"token": token})
    });
}

export const putGetCompanyInterviews = (companyId: string, token: string): Promise<Response> => {
    return fetch(`http://localhost:8080/getCompanyInterviews/${companyId}`, {
        method: "PUT",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({"token": token})
    });
}

export const postEvaluateStudent = (token: string, studentEvaluationFormFields: IStudentEvaluationFormFields): Promise<Response> => {
    return fetch(`http://localhost:8080/evaluateStudent/${token}`, {
        method: "POST",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(studentEvaluationFormFields)
    });
}

export const postCreateInterview = (companyDateOffers: string[],
                                    token: string,
                                    companyId: string,
                                    offerId: string,
                                    studentId: string,): Promise<Response> => {
    return fetch(`http://localhost:8080/createInterview`, {
        method: "POST",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            companyDateOffers: companyDateOffers,
            token: token,
            companyId: companyId,
            offerId: offerId,
            studentId: studentId,
        })
    });
}

export const putGetEvaluatedStudentsContracts = (token: string, companyId: string): Promise<Response> => {
    return fetch(`http://localhost:8080/getEvaluatedStudentsContracts/${companyId}`, {
        method: "PUT",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({"token": token})
    });
}

export const putCompanyNotification = (companyId: string, token: string): Promise<Response> => {
    return fetch(`http://localhost:8080/companyNotification/${companyId}`, {
        method: "PUT",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({"token": token})
    });
}