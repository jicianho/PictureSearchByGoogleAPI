package com.takepickpicturedemo;

public class ImageBean {

      private String hash;
      private String path;

     public ImageBean(String hash, String path) {
         setImageBean(hash, path);
     }

     public String getHash() {
         return hash;
     }

     public void setHash(String hash) {
         this.hash = hash;
     }

     public String getPath() {
         return path;
     }

     public void setPath(String path) {
         this.path = path;
     }



     /***************************************************************/
     public void setImageBean(String hash, String path) {
      setHash(hash);
      setPath(path);
     }

 }