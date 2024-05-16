process SUMMARIZEDEXPERIMENT_SUMMARIZEDEXPERIMENT {
    tag "$meta.id"
    label 'process_medium'

    conda "${moduleDir}/environment.yml"

    input:
    tuple val(meta), path(matrix_files)
    tuple val(meta2), path(rowdata)
    tuple val(meta3), path(coldata)

    output:
    tuple val(meta), path("*.rds")              , emit: rds
    tuple val(meta), path("*.R_sessionInfo.log"), emit: log
    path "versions.yml"                         , emit: versions

    when:
    task.ext.when == null || task.ext.when

    script:
    template 'summarizedexperiment.r'

    stub:
    """
    touch ${meta.id}.SummarizedExperiment.rds
    touch ${meta.id}.R_sessionInfo.log

    cat <<-END_VERSIONS > versions.yml
    "${task.process}":
        bioconductor-summarizedexperiment: \$(Rscript -e "library(SummarizedExperiment); cat(as.character(packageVersion('SummarizedExperiment')))")
    END_VERSIONS
    """
}
