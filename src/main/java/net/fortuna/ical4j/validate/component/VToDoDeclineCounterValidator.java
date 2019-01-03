package net.fortuna.ical4j.validate.component;

import net.fortuna.ical4j.model.component.VToDo;
import net.fortuna.ical4j.validate.ComponentValidator;
import net.fortuna.ical4j.validate.PropertyValidator;
import net.fortuna.ical4j.validate.ValidationException;
import net.fortuna.ical4j.validate.Validator;
import org.apache.commons.collections4.Closure;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Arrays;

import static net.fortuna.ical4j.model.Component.VALARM;
import static net.fortuna.ical4j.model.Property.*;

/**
 * <pre>
 * Component/Property   Presence
 * -------------------  ---------------------------------------------
 * METHOD               1       MUST be "DECLINECOUNTER"
 *
 * VTODO                1
 *     ATTENDEE         1+      MUST for all attendees
 *     DTSTAMP          1
 *     ORGANIZER        1
 *     SEQUENCE         1       MUST echo the original SEQUENCE number
 *     UID              1       MUST echo original UID
 *     ATTACH           0+
 *     CATEGORIES       0 or 1  This property may contain a list of values
 *     CLASS            0 or 1
 *     COMMENT          0 or 1
 *     CONTACT          0+
 *     CREATED          0 or 1
 *     DESCRIPTION      0 or 1
 *     DTSTART          0 or 1
 *     DUE              0 or 1  If present DURATION MUST NOT be present
 *     DURATION         0 or 1  If present DUE MUST NOT be present
 *     EXDATE           0+
 *     EXRULE           0+
 *     GEO              0 or 1
 *     LAST-MODIFIED    0 or 1
 *     LOCATION         0 or 1
 *     PERCENT-COMPLETE 0 or 1
 *     PRIORITY         0 or 1
 *     RDATE            0+
 *     RECURRENCE-ID    0 or 1  MUST only if referring to an instance of a
 *                              recurring calendar component.  Otherwise
 *                              it MUST NOT be present.
 *     RELATED-TO       0+
 *     REQUEST-STATUS   0+
 *     RESOURCES        0 or 1  This property MAY contain a list of values
 *     RRULE            0+
 *     STATUS           0 or 1  MAY be one of COMPLETED/NEEDS ACTION/IN-
 *                              PROCESS
 *     URL              0 or 1
 *     X-PROPERTY       0+
 *
 * VTIMEZONE            0+  MUST be present if any date/time refers to
 *                          a timezone
 * X-COMPONENT          0+
 *
 * VALARM               0
 * VEVENT               0
 * VFREEBUSY            0
 * </pre>
 *
 */
public class VToDoDeclineCounterValidator implements Validator<VToDo> {

    private static final long serialVersionUID = 1L;

    public void validate(final VToDo target) throws ValidationException {
        PropertyValidator.getInstance().assertOneOrMore(ATTENDEE, target.getProperties());

        PropertyValidator.getInstance().assertOne(DTSTAMP, target.getProperties());
        PropertyValidator.getInstance().assertOne(ORGANIZER, target.getProperties());
        PropertyValidator.getInstance().assertOne(SEQUENCE, target.getProperties());
        PropertyValidator.getInstance().assertOne(UID, target.getProperties());

        CollectionUtils.forAllDo(Arrays.asList(CATEGORIES, CLASS, CREATED, DESCRIPTION,
                DTSTART, DUE, DURATION, GEO, LAST_MODIFIED, LOCATION,
                LOCATION, PERCENT_COMPLETE, PRIORITY, RECURRENCE_ID, RESOURCES,
                STATUS, URL), new Closure<String>() {
            @Override
            public void execute(String input) {
                PropertyValidator.getInstance().assertOneOrLess(input, target.getProperties());
            }
        });

        ComponentValidator.assertNone(VALARM, target.getAlarms());
    }
}
