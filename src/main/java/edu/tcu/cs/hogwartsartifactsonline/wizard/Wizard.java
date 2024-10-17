package edu.tcu.cs.hogwartsartifactsonline.wizard;

import edu.tcu.cs.hogwartsartifactsonline.artifact.Artifact;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Wizard implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String name;

    /**
     * wizard has access to a list of known artifacts.
     * one wizard has many artifacts.
     * the artifact (many side) will maintain a foreign key (reference) to point to a wizard.
     * cascade: if a wizard is saved into the DB, then all the artifacts associated with the wizard are saved as well.
     */
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "owner")
    private List<Artifact> artifacts = new ArrayList<>();

    public Wizard() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Artifact> getArtifacts() {
        return artifacts;
    }

    public void setArtifacts(List<Artifact> artifacts) {
        this.artifacts = artifacts;
    }

    public void addArtifact(Artifact artifact) {
        /*
          * 1. set the owner of the artifact to the current wizard
          * 2. add this artifact to the wizard's artifact field.
         */
        artifact.setOwner(this); // #1 above
        this.artifacts.add(artifact); // #2 above
    }

    public Integer getNumberOfArtifacts() {
        return this.artifacts.size();
    }

    public void removeAllArtifacts() {
        this.artifacts.stream()
                .forEach(
                        artifact -> artifact.setOwner(null)
                );
        this.artifacts = null;
    }

    public void removeArtifact(Artifact artifactToBeAssigned) {
        // Remove artifact owner
        artifactToBeAssigned.setOwner(null);
        // remove the artifact from the artifactList of the owner.
        this.artifacts.remove(artifactToBeAssigned);

    }
}
