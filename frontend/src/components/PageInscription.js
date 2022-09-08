import {useState} from "react";

const PageInscription = ({onInscrire}) => {

    const [typePersonne, setTypePersonne] = useState('')
    const [departement, setDepartement] = useState('')
    const [courriel, setCourriel] = useState('')
    const [motDePasse, setMotDePasse] = useState('')
    const [validationMotDePasse, setValidationMotDePasse] = useState('')
    const [courrielErr, setCourrielErr] = useState(false)

    const validEmail = new RegExp(
        '^[a-zA-Z0-9._:$!%-]+@[a-zA-Z0-9.-]+.[a-zA-Z]$'
    );

    const onSubmit = (e) => {
        e.preventDefault()

        if (!typePersonne || !departement || !courriel || !motDePasse || !validationMotDePasse) {
            alert('Veuillez ajouter toutes les informations')
            return
        }

        if (motDePasse !== validationMotDePasse) {
            alert('Vérifier le mot de passe et le mot de passe ne correspondent pas')
            return
        }
        if(motDePasse.length < 8){
            alert("La longueur du mot de passe est supérieure à 7")
            return
        }
        if(!validEmail.test(courriel)){
            setCourrielErr(true)
            return;
        }

        onInscrire({typePersonne : typePersonne, departement : departement, courriel : courriel, motDePasse : motDePasse})
        setTypePersonne('')
        setDepartement('')
        setCourriel('')
        setMotDePasse('')
        setValidationMotDePasse('')
        setCourrielErr(false)
    }
    return(
        <div>
            <div>Inscription</div>
            <form onSubmit={onSubmit}>
                <div>
                    <label>
                        Type de personne :
                        <select defaultValue={'Choix un type'} onChange={(e) => setTypePersonne(e.target.value)}>
                            <option value="Choix un type" disabled>Choix un type</option>
                            <option value="Etudiant">Etudiant</option>
                            <option value="Entreprise">Entreprise</option>
                        </select>
                    </label>
                </div>
               <div>
                   <label>
                       Département :
                       <select defaultValue={'Choix un type'} onChange={(e) => setDepartement(e.target.value)}>
                           <option value="Choix un type" disabled>Choix un département</option>
                           <option value="Techniques de l’informatique">Techniques de l’informatique</option>
                           <option value="Techniques de la logistique du transport">Techniques de la logistique du transport</option>
                       </select>
                   </label>
               </div>

                <div className='form-control'>
                    <label>Courriel</label>
                    <input type='email' placeholder='Courriel'
                           value={courriel}
                           onChange={(e) => setCourriel(e.target.value)}
                           required/>
                </div>
                {courrielErr && <p>Your email is invalid</p>}
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
                <input type='submit' value='Inscrire' />
            </form>
        </div>

    )
}
export default PageInscription