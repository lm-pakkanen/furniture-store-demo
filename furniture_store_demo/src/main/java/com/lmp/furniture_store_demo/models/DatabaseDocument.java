package com.lmp.furniture_store_demo.models;

import org.springframework.lang.Nullable;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

/**
 * Database document superclass.
 * Used by all database models.
 */
@MappedSuperclass
public class DatabaseDocument {
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.UUID)
  private @Nullable String id;

  public DatabaseDocument(String id) {
    this.id = id;
  }

  public DatabaseDocument() {
  }

  public @Nullable String getId() {
    return this.id;
  }

  public void setId(@Nullable String id) {
    this.id = id;
  }
}
