package com.example.trokyy.interfaces;

import java.util.ArrayList;
import java.util.List;

public interface IType<T> {
    void Add (T t);
    void Update(T t);
    void DeleteByID(int id);
    List<T> afficher();
    ArrayList<T> getAll();
}
