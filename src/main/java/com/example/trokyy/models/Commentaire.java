package com.example.trokyy.models;

public class Commentaire {



        private int id;
        private int commenteur_id;
        private int blog_id;
        private String contenu;


        // Constructeur

        public Commentaire() {
        }

        public Commentaire(int id, int commenteur_id, int blog_id, String contenu) {
            this.id = id;
            this.commenteur_id = commenteur_id;
            this.blog_id = blog_id;
            this.contenu = contenu;

        }

        //getter and setter

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getCommenteur_id() {
            return commenteur_id;
        }

        public void setCommenteur_id(int commenteur_id) {
            this.commenteur_id = commenteur_id;
        }

        public int getBlog_id() {
            return blog_id;
        }

        public void setBlog_id(int blog_id) {
            this.blog_id = blog_id;
        }

        public String getContenu() {
            return contenu;
        }

        public void setContenu(String contenu) {
            this.contenu = contenu;
        }


    }

