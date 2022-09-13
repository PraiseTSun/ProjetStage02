package projet.projetstage02.service;

public abstract class AbstractService<T> {
    public abstract T getUserById(Long id);
}
