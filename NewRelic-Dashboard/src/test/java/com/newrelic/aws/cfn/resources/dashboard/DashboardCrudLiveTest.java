package com.newrelic.aws.cfn.resources.dashboard;

import com.google.common.collect.ImmutableList;
import com.newrelic.aws.cfn.resources.dashboard.nerdgraph.schema.DashboardEntityResult;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Tag("Live")
public class DashboardCrudLiveTest extends AbstractResourceCrudLiveTest<DashboardResourceHandler, DashboardEntityResult, Pair<Integer, String>, ResourceModel, CallbackContext, TypeConfigurationModel> {

    @Override
    protected TypeConfigurationModel newTypeConfiguration() throws Exception {
        return TypeConfigurationModel.builder()
                .endpoint(System.getenv("NR_ENDPOINT"))
                .apiKey(System.getenv("NR_API_KEY"))
                .build();
    }

    @Override
    protected ResourceModel newModelForCreate() throws Exception {
        int accountId = Integer.parseInt(System.getenv("NR_ACCOUNT_ID"));

        return ResourceModel.builder()
                .accountId(accountId)
                .dashboard(DashboardInput.builder()
                        .name("My Dashboard")
                        .description("Dashboard for my new app")
                        .pages(ImmutableList.of(PageInput.builder()
                                .name("Page 1")
                                .description("Page 1 of my new dashboard")
                                .widgets(ImmutableList.of(
                                        WidgetInput.builder()
                                                .configuration(
                                                        WidgetInputConfigurationInput.builder()
                                                                .line(TypeWidgetInputConfigurationInputInput.builder()
                                                                        .nrqlQueries(ImmutableList.of(NrqlQueryInput.builder()
                                                                                .accountId(accountId)
                                                                                .query("SELECT count(*) FROM Transaction FACET appName TIMESERIES")
                                                                                .build()))
                                                                        .build())
                                                                .build())
                                                .title("Widget A")
                                                .build()
                                ))
                                .build()))
                        .permissions("PRIVATE")
                        .build())
                .build();
    }

    @Override
    protected HandlerWrapper newHandlerWrapper() {
        return new HandlerWrapper();
    }
}
