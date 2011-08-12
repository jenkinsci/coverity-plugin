package jenkins.plugins.coverity;

import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Action;
import hudson.model.Hudson;
import hudson.tasks.test.AbstractTestResultAction;
import hudson.util.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.StackedAreaRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.ui.RectangleInsets;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.Calendar;

/**
 * Project-level action for Coverity. This is used to to display the history graph.
 */
public class CoverityProjectAction implements Action {

    private final AbstractProject<?,?> project;

    public CoverityProjectAction(AbstractProject<?,?> project) {
        this.project = project;
    }

    public AbstractProject<?,?> getProject() {
        return project;
    }

    public String getIconFileName() {
        return null;
    }

    public String getDisplayName() {
        return null;
    }

    public String getUrlName() {
        return "coverity";
    }

    public Graph getGraph() {
        return new GraphImpl();
    }

    private class GraphImpl extends Graph {
        protected GraphImpl() {
            super(project.getLastCompletedBuild() != null ? project.getLastCompletedBuild().getTimestamp() : Calendar.getInstance(), 600,300);
        }

        protected DataSetBuilder<String, ChartLabel> createDataSet() {
            DataSetBuilder<String, ChartLabel> data = new DataSetBuilder<String, ChartLabel>();
            AbstractBuild<?,?> build = project.getLastCompletedBuild();
            while (build != null) {
                final CoverityBuildAction action = build.getAction(CoverityBuildAction.class);
                if (action != null) {
                    data.add(action.getDefectIds().size(), "", new ChartLabel(build)  {
                        @Override
                        public Color getColor() {
                            if (action.getDefectIds().size() > 0)
                                return ColorPalette.RED;
                            else
                                return ColorPalette.BLUE;
                        }
                    });
                }
                build = build.getPreviousBuild();
            }
            return data;

        }

        protected JFreeChart createGraph() {
            final CategoryDataset dataset = createDataSet().build();

            final JFreeChart chart = ChartFactory.createStackedAreaChart(null, // chart
                    // title
                    null, // unused
                    "Defect Count", // range axis label
                    dataset, // data
                    PlotOrientation.VERTICAL, // orientation
                    false, // include legend
                    true, // tooltips
                    false // urls
            );

            chart.setBackgroundPaint(Color.white);

            final CategoryPlot plot = chart.getCategoryPlot();

            // plot.setAxisOffset(new Spacer(Spacer.ABSOLUTE, 5.0, 5.0, 5.0, 5.0));
            plot.setBackgroundPaint(Color.WHITE);
            plot.setOutlinePaint(null);
            plot.setForegroundAlpha(0.8f);
            // plot.setDomainGridlinesVisible(true);
            // plot.setDomainGridlinePaint(Color.white);
            plot.setRangeGridlinesVisible(true);
            plot.setRangeGridlinePaint(Color.black);

            CategoryAxis domainAxis = new ShiftedCategoryAxis(null);
            plot.setDomainAxis(domainAxis);
            domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);
            domainAxis.setLowerMargin(0.0);
            domainAxis.setUpperMargin(0.0);
            domainAxis.setCategoryMargin(0.0);

            final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
            ChartUtil.adjustChebyshev(dataset, rangeAxis);
            rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
            rangeAxis.setAutoRange(true);

            StackedAreaRenderer ar = new StackedAreaRenderer2() {
                @Override
                public Paint getItemPaint(int row, int column) {
                    ChartLabel key = (ChartLabel) dataset.getColumnKey(column);
                    if (key.getColor() != null) return key.getColor();
                    return super.getItemPaint(row, column);
                }

                @Override
                public String generateURL(CategoryDataset dataset, int row,
                        int column) {
                    ChartLabel label = (ChartLabel) dataset.getColumnKey(column);
                    return label.getUrl();
                }

                @Override
                public String generateToolTip(CategoryDataset dataset, int row,
                        int column) {
                    ChartLabel label = (ChartLabel) dataset.getColumnKey(column);
                    return label.build.getDisplayName() + " has "
                            + label.build.getAction(CoverityBuildAction.class).getDefectIds().size() + " defects";
                }
            };
            plot.setRenderer(ar);
            ar.setSeriesPaint(0,ColorPalette.RED); // Failures.
            ar.setSeriesPaint(1,ColorPalette.YELLOW); // Skips.
            ar.setSeriesPaint(2,ColorPalette.BLUE); // Total.

            // crop extra space around the graph
            plot.setInsets(new RectangleInsets(0, 0, 0, 5.0));

            return chart;
        }
    }

    class ChartLabel implements Comparable<ChartLabel> {
        private AbstractBuild build;
        public ChartLabel(AbstractBuild build) {
            this.build = build;
        }

        public String getUrl() {
            return Hudson.getInstance().getRootUrl() + build.getUrl() + "coverity";
        }

        public int compareTo(ChartLabel that) {
            return build.number - that.build.number;
        }

        @Override
        public boolean equals(Object o) {
        	if (!(o instanceof ChartLabel)) {
            	return false;
            }
            ChartLabel that = (ChartLabel) o;
            return this.build == that.build;
        }

        public Color getColor() {
        	return null;
        }

        @Override
        public int hashCode() {
            return build.getDisplayName().hashCode();
        }

        @Override
        public String toString() {
            String l = build.getDisplayName();
            String s = build.getBuiltOnStr();
            if (s != null)
                l += ' ' + s;
            return l;
        }

    }
}
