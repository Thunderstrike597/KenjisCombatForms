package net.kenji.kenjiscombatforms.api.utils;

import yesman.epicfight.gameasset.EpicFightSkills;
import yesman.epicfight.skill.Skill;

import java.lang.reflect.Field;

public class SkillUtils {

    // Method to get a skill by name dynamically using reflection
    public static Skill getSkillByName(String skillName) {
        try {
            // Get all fields from the EpicFightSkills class
            Field[] fields = EpicFightSkills.class.getDeclaredFields();

            // Loop through each field
            for (Field field : fields) {
                // Check if the field is of type Skill
                if (Skill.class.isAssignableFrom(field.getType())) {
                    field.setAccessible(true); // Make sure the field is accessible
                    Skill skill = (Skill) field.get(null); // Get the static field value
                    // Compare the name (or ID) of the skill
                    if (skill != null && skill.toString().equalsIgnoreCase(skillName)) {
                        return skill; // Return the matching skill
                    }
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null; // Return null if no match is found
    }
}

