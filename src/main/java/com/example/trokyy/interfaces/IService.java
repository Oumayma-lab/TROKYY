package com.example.trokyy.interfaces;
import java.util.List;
public interface IService<T>{

    public void addReclamation(T t);
    public void deleteReclamation(T t);

    public void updateReclamation(T t);
    public List<T> getData();

}
