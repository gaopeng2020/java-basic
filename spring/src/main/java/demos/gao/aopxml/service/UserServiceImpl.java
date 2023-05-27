package demos.gao.aopxml.service;

public class UserServiceImpl implements IUserService {
    /**
     * add a user
     */
    @Override
    public void add() {
        System.out.println("add a user");
    }

    /**
     * delete a user
     */
    @Override
    public void delete() {
        System.out.println("delete a user");
    }

    /**
     * modify a user
     */
    @Override
    public void update() {
        System.out.println("update a user");
    }

    /**
     * query a user
     */
    @Override
    public void query() {
        System.out.println("query a user");
    }
}
