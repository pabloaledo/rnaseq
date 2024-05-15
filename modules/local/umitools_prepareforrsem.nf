process UMITOOLS_PREPAREFORRSEM {
    tag "$meta.id"
    label 'process_medium'

    conda "umi_tools=1.1.2"

    input:
    tuple val(meta), path(bam)

    output:
    tuple val(meta), path('*.bam'), emit: bam
    tuple val(meta), path('*.log'), emit: log
    path  "versions.yml"          , emit: versions

    when:
    task.ext.when == null || task.ext.when

    script: // This script is bundled with the pipeline, in nf-core/rnaseq/bin/
    def args = task.ext.args ?: ''
    def prefix = task.ext.prefix ?: "${meta.id}"
    """
    prepare-for-rsem.py \\
        --stdin=$bam \\
        --stdout=${prefix}.bam \\
        --log=${prefix}.prepare_for_rsem.log \\
        $args

    cat <<-END_VERSIONS > versions.yml
    "${task.process}":
        umitools: \$(umi_tools --version 2>&1 | sed 's/^.*UMI-tools version://; s/ *\$//')
    END_VERSIONS
    """
}
