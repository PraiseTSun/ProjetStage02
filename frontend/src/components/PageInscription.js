import {useState} from "react";

const PageInscription = ({}) => {
    const [typePersonne, setTypePersonne] = useState('')
    const [departement, setDepartement] = useState('')
    const [courriel, setCourriel] = useState('')
    const [motDePasse, setMotDePasse] = useState('')
    const [validationMotDePasse, setValidationMotDePasse] = useState('')
    return(
        <div>
            <div>Inscription</div>
            <form>
                <div>
                    <label>
                        Type de personne :
                        <select   onChange={(e) => setTypePersonne(e.target.value)}>
                            <option value="Etudiant">Etudiant</option>
                            <option value="Entreprise">Entreprise</option>
                        </select>
                    </label>
                </div>
               <div>
                   <label>
                       Departement :
                       <select  onChange={(e) => setDepartement(e.target.value)}>
                           <option value="Techniques de l’informatique">Techniques de l’informatique</option>
                           <option value="Techniques de la logistique du transport">Techniques de la logistique du transport</option>
                       </select>
                   </label>
               </div>

                <div className='form-control'>
                    <label>Nom</label>
                    <input type='text' placeholder='Courriel'
                           value={courriel}
                           onChange={(e) => setCourriel(e.target.value)}/>
                </div>
                <div className='form-control'>
                    <label>Mot de pass</label>
                    <input type='text' placeholder='Mot de passe'
                           value={motDePasse}
                           onChange={(e) => setMotDePasse(e.target.value)}/>
                </div>
                <div className='form-control'>
                    <label>Vérifier votre mot de passe</label>
                    <input type='text' placeholder='Confirmation mot de passe'
                           value={validationMotDePasse}
                           onChange={(e) => setValidationMotDePasse(e.target.value)}/>
                </div>
            </form>
        </div>

    )
}
export default PageInscription