process RSEQC_READDISTRIBUTION {
    tag "$meta.id"
    label 'process_medium'

    conda "rseqc=3.0.1 'r-base>=3.5'"

    input:
    tuple val(meta), path(bam)
    path  bed

    output:
    tuple val(meta), path("*.read_distribution.txt"), emit: txt
    path  "versions.yml"                            , emit: versions

    when:
    task.ext.when == null || task.ext.when

    script:
    def args = task.ext.args ?: ''
    def prefix = task.ext.prefix ?: "${meta.id}"
    """
    read_distribution.py \\
        -i $bam \\
        -r $bed \\
        > ${prefix}.read_distribution.txt

    cat <<-END_VERSIONS > versions.yml
    "${task.process}":
        rseqc: \$(read_distribution.py --version | sed -e "s/read_distribution.py //g")
    END_VERSIONS
    """
}
