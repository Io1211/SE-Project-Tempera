package at.qe.skeleton.tests;

import at.qe.skeleton.model.Project;
import at.qe.skeleton.model.SubordinateTimeRecord;
import at.qe.skeleton.model.SuperiorTimeRecord;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.model.enums.UserxRole;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.parameters.P;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Tests to ensure that each entity's implementation of equals conforms to the
 * contract. See {@linkplain http://www.jqno.nl/equalsverifier/} for more
 * information.
 *
 * This class is part of the skeleton project provided for students of the
 * course "Software Engineering" offered by the University of Innsbruck.
 */
public class EqualsImplementationTest {

    @Test
    public void testUserEqualsContract() {
        Userx user1 = new Userx();
        user1.setUsername("user1");
        Userx user2 = new Userx();
        user2.setUsername("user2");
        EqualsVerifier.forClass(Userx.class).withPrefabValues(Userx.class, user1, user2).suppress(Warning.STRICT_INHERITANCE, Warning.ALL_FIELDS_SHOULD_BE_USED).verify();
    }

    @Test
    public void testUserRoleEqualsContract() {
        EqualsVerifier.forClass(UserxRole.class).verify();
    }



}