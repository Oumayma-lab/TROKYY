package com.example.trokyy.interfaces;

import java.util.List;

public interface IEvenement<T>{
    void Add (T t);
    void Update(T t);
    void DeleteByID(int id);
    List<T> afficher();
    List<T> TriparTitre();
    List<T> TriparLieu();
    List<T> Rechreche(String recherche);
}
