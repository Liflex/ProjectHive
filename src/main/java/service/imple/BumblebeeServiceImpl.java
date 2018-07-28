package service.imple;



import dao.imple.BumblebeeDAOImpl;
import dao.interf.IBumblebeeDAO;
import model.Bumblebee;
import service.interf.IBumblebeeService;
import util.MySqlConnector;

import java.util.List;

public class BumblebeeServiceImpl implements IBumblebeeService {

    private final IBumblebeeDAO bumblebeeDAO;

    public BumblebeeServiceImpl() {
       bumblebeeDAO = new BumblebeeDAOImpl(MySqlConnector.getSessionFactory());
    }

    @Override
    public Bumblebee get(long id) {
        return bumblebeeDAO.get(id);
    }

    @Override
    public List getAll() {
        return bumblebeeDAO.getAll();
    }

    @Override
    public void add(Bumblebee o) {
        bumblebeeDAO.add(o);
    }

    @Override
    public void update(Bumblebee user) {
        bumblebeeDAO.update(user);
    }

    @Override
    public void delete(long id) {
        bumblebeeDAO.delete(id);
    }
}
