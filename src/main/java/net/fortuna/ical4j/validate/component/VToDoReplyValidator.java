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
 * METHOD               1      MUST be "REPLY"
 * VTODO                1+     All component MUST have the same UID
 *     ATTENDEE         1+
 *     DTSTAMP          1
 *     ORGANIZER        1
 *     UID              1      MUST must be the address of the replying
 *                             attendee
 *     REQUEST-STATUS   0+
 *     ATTACH           0+
 *     CATEGORIES       0 or 1 This property may contain a list of values
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
 *     RELATED-TO       0+
 *     RESOURCES        0 or 1  This property may contain a list of values
 *     RRULE            0+
 *     RECURRENCE-ID    0 or 1  MUST only if referring to an instance of a
 *                              Recurring calendar component. Otherwise it
 *                              MUST NOT be present
 *     SEQUENCE         0 or 1  MUST be the sequence number of
 *                              the original REQUEST if greater than 0.
 *                              MAY be present if 0.
 *     STATUS           0 or 1
 *     SUMMARY          0 or 1  Can be null
 *     URL              0 or 1
 *     X-PROPERTY       0+
 *
 * VTIMEZONE            0 or 1  MUST be present if any date/time refers to
 *                              a timezone
 * X-COMPONENT          0+
 *
 * VALARM               0
 * VEVENT               0
 * VFREEBUSY            0
 * </pre>
 *
 */
public class VToDoReplyValidator implements Validator<VToDo> {

    private static final long serialVersionUID = 1L;

    public void validate(final VToDo target) throws ValidationException {
        PropertyValidator.getInstance().assertOneOrMore(ATTENDEE, target.getProperties());

        PropertyValidator.getInstance().assertOne(DTSTAMP, target.getProperties());
        PropertyValidator.getInstance().assertOne(ORGANIZER, target.getProperties());
        PropertyValidator.getInstance().assertOne(UID, target.getProperties());

        CollectionUtils.forAllDo(Arrays.asList(CATEGORIES, CLASS, CREATED, DESCRIPTION,
                DTSTART, DUE, DURATION, GEO, LAST_MODIFIED, LOCATION,
                PERCENT_COMPLETE, PRIORITY, RESOURCES, RECURRENCE_ID, SEQUENCE,
                STATUS, SUMMARY, URL), new Closure<String>() {
            @Override
            public void execute(String input) {
                PropertyValidator.getInstance().assertOneOrLess(input, target.getProperties());
            }
        });

        ComponentValidator.assertNone(VALARM, target.getAlarms());
    }
}
