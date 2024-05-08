package com.example.trokyy.interfaces;

import java.util.List;

public interface ICommentaire<T> {
    public void addCommentaire(T t);

    public void deleteCommentaire(T t);

    public void updateCommentaire(T t);

    public List<T> recupererCommentaire();
}