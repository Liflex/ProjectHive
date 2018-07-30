package dao.interf;

import model.Bumblebee;

import java.util.List;

public interface BumblebeeDAOInterf {

    Bumblebee get(long id);
    List<Bumblebee> getAll();
    void add(Bumblebee t);
    void update(Bumblebee t);
    void delete(long id);
}
