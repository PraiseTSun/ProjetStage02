import IOffer from "../../models/IOffer";

export const postCreateOffre = (offer: IOffer): Promise<Response> => {
    return fetch("http://localhost:8080/createOffre", {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify(offer)
    });
};

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