package hu.karsany.plsqlfromjava;

import hu.karsany.plsqlfromjava.generator.*;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;

/**
 * Generates the Java classes for PL/SQL
 *
 * @goal "generate"
 */
@Mojo(name = "generate", requiresProject = true)
public class PlsqlFromJavaMojo extends AbstractMojo {

    /**
     * Base directory of the project.
     */
    @Parameter(property = "pfj.project.outputDirectory", defaultValue = "${basedir}\\src\\main\\java\\")
    private File outputDirectory;

    /**
     * Main package.
     */
    @Parameter(property = "pfj.project.package", defaultValue = "${project.groupId}")
    private String packageName;


    /**
     * JDBC Connection URL with username and password
     */
    @Parameter(property = "pfj.jdbc.url", defaultValue = "jdbc:oracle:thin:tiger/password@localhost:1521:xe")
    private String jdbcUrl;

    /**
     * Target Java subpackage for generated entity classes
     */
    @Parameter(property = "pfj.package.entity", defaultValue = "database.entity")
    private String entityPackage;

    /**
     * Target Java subpackage for generated converter classes
     */
    @Parameter(property = "pfj.package.converter", defaultValue = "database.converter")
    private String converterPackage;

    /**
     * Target Java subpackage for generated util classes
     */
    @Parameter(property = "pfj.package.util", defaultValue = "database.util")
    private String utilPackage;

    /**
     * Target Java subpackage for generated procedure classes
     */
    @Parameter(property = "pfj.package.procedure", defaultValue = "database.procedure")
    private String procedurePackage;

    /**
     * Target Java subpackage for generated procedure classes
     */
    @Parameter(property = "pfj.package.service", defaultValue = "database.service")
    private String servicePackage;


    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info(this.toString());

        Settings settings = new Settings();
        settings.setJdbcUrl(jdbcUrl);
        settings.setBasePath(outputDirectory.toString());
        settings.setEntityPackageName(packageName + "." + entityPackage);
        settings.setConverterPackageName(packageName + "." + converterPackage);
        settings.setUtilPackageName(packageName + "." + utilPackage);
        settings.setProcedurePackageName(packageName + "." + procedurePackage);
        settings.setServicePackageName(packageName + "." + servicePackage);

        new UtilsGenerator().generate(settings);
        new ObjectTypeGenerator().generate(settings);
        new ObjectTypeConverterGenerator().generate(settings);
        new ProcedureGenerator().generate(settings);
        new ServiceGenerator().generate(settings);

    }

    @Override
    public String toString() {
        return "plsql-from-java-maven-plugin:\n" +
                "  pfj.project.outputDirectory = " + outputDirectory + "\n" +
                "  pfj.project.package         = " + packageName + "\n" +
                "  pfj.jdbc.url                = " + jdbcUrl + "\n" +
                "  pfj.package.entity          = " + entityPackage + "\n" +
                "  pfj.package.converter       = " + converterPackage + "\n" +
                "  pfj.package.util            = " + utilPackage + "\n" +
                "  pfj.package.procedure       = " + procedurePackage + "\n" +
                "  pfj.package.service         = " + servicePackage + "\n";
    }
}
