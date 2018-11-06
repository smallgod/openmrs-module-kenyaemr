package org.openmrs.module.kenyaemr.reporting.cohort.definition.evaluator.pmtct.anc;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Cohort;
import org.openmrs.annotation.Handler;
import org.openmrs.api.context.Context;
import org.openmrs.module.kenyaemr.reporting.EmrReportingUtils;
import org.openmrs.module.kenyaemr.reporting.cohort.definition.pmtct.anc.StartedHAARTInANCCohortDefinition;
import org.openmrs.module.kenyaemr.reporting.library.ETLReports.MOH731Greencard.ETLMoh731GreenCardCohortLibrary;
import org.openmrs.module.reporting.cohort.EvaluatedCohort;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.evaluator.CohortDefinitionEvaluator;
import org.openmrs.module.reporting.cohort.definition.service.CohortDefinitionService;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.EvaluationException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Evaluator for patients who started haart in ANC Register
 */
import java.util.Calendar;
import java.util.Date;
import java.util.Map;


@Handler(supports = {StartedHAARTInANCCohortDefinition.class})
public class StartedHAARTInANCCohortDefinitionEvaluator implements CohortDefinitionEvaluator {

	private final Log log = LogFactory.getLog(this.getClass());
	@Autowired
	private ETLMoh731GreenCardCohortLibrary moh731GreencardCohorts;

	@Override
	public EvaluatedCohort evaluate(CohortDefinition cohortDefinition, EvaluationContext context) throws EvaluationException {

		StartedHAARTInANCCohortDefinition definition = (StartedHAARTInANCCohortDefinition) cohortDefinition;
		CohortDefinition cd = moh731GreencardCohorts.startedHAARTAtANC();

		Calendar calendar = Calendar.getInstance();
		int thisMonth = calendar.get(calendar.MONTH);

		Map<String, Date> dateMap = EmrReportingUtils.getReportDates(thisMonth - 1);
		Date startDate = dateMap.get("startDate");
		Date endDate = dateMap.get("endDate");

		context.addParameterValue("startDate", startDate);
		context.addParameterValue("endDate", endDate);

		Cohort startedHAARTAtANC = Context.getService(CohortDefinitionService.class).evaluate(cd, context);


		return new EvaluatedCohort(startedHAARTAtANC, definition, context);
	}
}




