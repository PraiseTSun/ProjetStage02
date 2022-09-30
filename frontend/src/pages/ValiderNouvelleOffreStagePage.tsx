import React, {useEffect, useState} from "react";
import {Button, Container, Table} from "react-bootstrap";
import {Link} from "react-router-dom";
import IOffre from "../models/IOffre";


const ValiderNouvelleOffreStagePage = ({setPdfId}: { setPdfId: Function }): JSX.Element => {
    const emptyOffre: IOffre = {
        id: 0,
        nomDeCompagnie: "",
        department: "",
        position: "",
        heureParSemaine: 0,
        adresse: "",
    }
    const [offresAValider, setOffresAValider] = useState([emptyOffre])

    const deleteOffre = async (id: number) => {
        const res = await fetch(`http://localhost:8080/removeOffer/${id}`,
            {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(id)
            })
    }

    useEffect(() => {
        fetchOffresAttendreValide()
    }, [])
    const fetchOffresAttendreValide = async () => {
        const res = await fetch(`http://localhost:8080/unvalidatedOffers`,
        {
            method: 'GET',
                headers: {
            'Content-Type': 'application/json',
        }
        })
        const data = await res.json()

        if (res.status === 200) {
            setOffresAValider(data)
        }
    }

    const valideOffre = async (id: number) => {
        const res = await fetch(`http://localhost:8080/validateOffer/${id}`,
            {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(id)
            })

    }
    const setId = (offreId: number) => {
        setPdfId(offreId)
    }

    if (offresAValider.length == 1 && offresAValider[0] == emptyOffre) {
        return <>
            <Link to="/" className="btn btn-primary my-3">Home</Link>
            <h1>Aucune offre à valider</h1>
        </>
    }
    return (<>
            <Link to="/" className="btn btn-primary my-3">Home</Link>
            <Container className="justify-content-center">
                <h1 className="card-header text-center mb-2 p-5">Valider les nouveaux offres</h1>
                <Table striped bordered hover bgcolor="Azure">
                    <thead>
                    <tr className="text-uppercase">
                        <th>Nom De Compagnie</th>
                        <th>Départment / Position</th>
                        <th>Heure Par Semaine / Adresse</th>
                        <th>Pdf</th>
                        <th>Valide</th>
                        <th>Non Valide</th>
                    </tr>
                    </thead>
                    <tbody>
                    {offresAValider.map((offre: any) => {
                        console.log(offre.id)
                        return (
                            <tr key={offre.id}>
                                <td>{offre.nomDeCompagnie}</td>
                                <td>{offre.department}<br/>{offre.position}</td>
                                <td>Heure : {offre.heureParSemaine}<br/>{offre.adresse}</td>
                                <td>
                                    <Link to={"/pdf"} onClick={() => setId(offre.id)} className="btn btn-warning ">Ouvrir
                                        PDF</Link>
                                </td>
                                <td><Button className="bg-success" onClick={() => {
                                    valideOffre(offre.id)
                                }}>Valide</Button></td>
                                <td><Button className="bg-danger" onClick={() => {
                                    deleteOffre(offre.id)
                                }}>Non Valide</Button></td>
                            </tr>
                        )
                    })}
                    </tbody>
                </Table>
            </Container>
        </>
    );
}
export default ValiderNouvelleOffreStagePage;
