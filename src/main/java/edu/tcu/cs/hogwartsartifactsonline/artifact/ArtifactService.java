package edu.tcu.cs.hogwartsartifactsonline.artifact;

import edu.tcu.cs.hogwartsartifactsonline.artifact.utils.IdWorker;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The transactional annotation makes database transactions a smoother process (will update this documentation).
 */
@Service
@Transactional
public class ArtifactService {

    private final ArtifactRepository artifactRepository;

    private final IdWorker idWorker;

    public ArtifactService(ArtifactRepository artifactRepository, IdWorker idWorker) {
        this.artifactRepository = artifactRepository;
        this.idWorker = idWorker;
    }

    public Artifact findById(String artifactId) {
//        Optional<Artifact> optional = artifactRepository.findById(artifactId);
//        if(optional.isEmpty()) throw new RuntimeException("can't find artifact");
//        Artifact artifact = optional.get();

        //        return artifactRepository.findById(artifactId);
//        return null;

//        return this.artifactRepository.findById(artifactId).get();
        /*
         * We try to findById using the artifactRepository and if found we just return
         * to the artifactController or else an exception is thrown that is of type
         * ArtifactNotFoundException with a custom message.
         */
        return this.artifactRepository.findById(artifactId)
                .orElseThrow(() -> new ArtifactNotFoundException(artifactId));
    }

    public List<Artifact> findAll() {
        return this.artifactRepository.findAll();
    }

    public Artifact save(Artifact newArtifact) {
        // server side's responsibility to generate the id
        // Twitter Snowflake Algorithm to assign id.
        newArtifact.setId(idWorker.nextId() + ""); // string concatenation to convert from long to string.
        return this.artifactRepository.save(newArtifact);
    }

    public Artifact update(String artifactId, Artifact update) {
        /*
         * use of a fluent interface
         * fluent interface is an object-oriented API whose design relies extensively
         * on method chaining to increase code legibility.
         */
//        Artifact oldArtifact = this.artifactRepository.findById(artifactId).get();
//        oldArtifact.setName(update.getName());
//        oldArtifact.setDescription(update.getDescription());
//        oldArtifact.setImageUrl(update.getImageUrl());
//        Artifact updatedArtifact = this.artifactRepository.save(oldArtifact);
//        return updatedArtifact;
        return this.artifactRepository.findById(artifactId)
                .map(oldArtifact -> {
                    // note: oldArtifact is the artifact that has been found.
                    // we go to the throw block below if the artifact is not found.
                    oldArtifact.setName(update.getName());
                    oldArtifact.setDescription(update.getDescription());
                    oldArtifact.setImageUrl(update.getImageUrl());
                    return this.artifactRepository.save(oldArtifact);
                })
                .orElseThrow(() -> new ArtifactNotFoundException(artifactId));
    }

    public void delete(String artifactId) {
        this.artifactRepository.findById(artifactId)
                .orElseThrow(() ->
                        new ArtifactNotFoundException(artifactId)
                );
        this.artifactRepository.deleteById(artifactId);
    }
}
