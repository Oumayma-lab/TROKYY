package com.example.trokyy.interfaces;

import java.util.List;

public interface IReponseService <T>{
    public void addReponse(T t);
    public void deleteReponse(T t);

    public void updateReponse(T t);
    public List<T> getDataReponse();

}
