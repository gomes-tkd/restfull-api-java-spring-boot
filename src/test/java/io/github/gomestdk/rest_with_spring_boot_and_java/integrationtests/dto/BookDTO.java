package io.github.gomestdk.rest_with_spring_boot_and_java.integrationtests.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class BookDTO implements Serializable{
  private static final long serialVersionUID = 1L;

  private long id;
  private String title;
  private String author;
  private Double price;
  private Date launchDate;
  
  public BookDTO() {
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public Double getPrice() {
    return price;
  }

  public void setPrice(Double price) {
    this.price = price;
  }

  public Date getLaunchDate() {
    return launchDate;
  }

  public void setLaunchDate(Date launchDate) {
    this.launchDate = launchDate;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    BookDTO bookDTO = (BookDTO) o;
    return id == bookDTO.id && Objects.equals(title, bookDTO.title) && Objects.equals(author, bookDTO.author) && Objects.equals(price, bookDTO.price) && Objects.equals(launchDate, bookDTO.launchDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, title, author, price, launchDate);
  }
}
