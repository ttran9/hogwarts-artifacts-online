package edu.tcu.cs.hogwartsartifactsonline.wizard;

import edu.tcu.cs.hogwartsartifactsonline.artifact.utils.IdWorker;
import edu.tcu.cs.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WizardService {

    private final WizardRepository wizardRepository;
    private final IdWorker idWorker;

    public WizardService(WizardRepository wizardRepository, IdWorker idWorker) {
        this.wizardRepository = wizardRepository;
        this.idWorker = idWorker;
    }

    public Wizard findWizardById(int wizardId) {
        // #2
//        Optional<Wizard> wizardOptional = wizardRepository.findById(wizardId);
//        if (wizardOptional.isEmpty()) throw new WizardNotFoundException(wizardId);
//        return wizardOptional.get();

        // #1
//        int wizardIdAsInteger = Integer.parseInt(wizardId);
//        return wizardRepository.findById(Integer.parseInt(wizardId)).orElseThrow(() -> new WizardNotFoundException(wizardId));

//        return wizardRepository.findBy(wizardId + "");

        // #3
        return this.wizardRepository.findById(wizardId)
                .orElseThrow(() -> new ObjectNotFoundException("wizard", wizardId));
    }

    public List<Wizard> findAll() {
        return this.wizardRepository.findAll();
    }

    public Wizard save(Wizard wizard) {
        wizard.setId((int) idWorker.nextId());
        return this.wizardRepository.save(wizard);
    }

    public Wizard update(int wizardId, Wizard newWizard) {
//        Wizard oldWizard = this.findWizardById(wizardId);
//        oldWizard.setName(newWizard.getName());
//        Wizard updatedWizard = this.wizardRepository.save(oldWizard);
//        return updatedWizard;
        return this.wizardRepository.findById(wizardId)
                .map(oldWizard -> {
                    oldWizard.setName(newWizard.getName());
                    return wizardRepository.save(oldWizard);
                })
                .orElseThrow(() -> new ObjectNotFoundException("wizard", wizardId));
    }

    public void delete(int wizardId) {
        Wizard wizardToBeDeleted = this.wizardRepository.findById(wizardId)
                .orElseThrow(() ->
                    new ObjectNotFoundException("wizard", wizardId)
                );

        wizardToBeDeleted.removeAllArtifacts();
        this.wizardRepository.deleteById(wizardId);
    }


}
