package com.katelocate.menugenerator.recipe;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

import jakarta.validation.constraints.NotEmpty;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.Id;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "recipe")
public class Recipe {
    @Id
    public Integer id;

    @NotEmpty
    public String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "recipe_type")
    public RecipeType recipeType;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(columnDefinition = "text[]")
    public List<String> instructions;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(columnDefinition = "text[]")
    public List<String> ingredients;
};
