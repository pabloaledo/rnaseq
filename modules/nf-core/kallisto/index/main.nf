process KALLISTO_INDEX {
    tag "$fasta"
    label 'process_medium'

    conda "${moduleDir}/environment.yml"

    input:
    tuple val(meta), path(fasta)

    output:
    tuple val(meta), path("kallisto")  , emit: index
    path "versions.yml"                , emit: versions

    when:
    task.ext.when == null || task.ext.when

    script:
    def args = task.ext.args ?: ''
    """
    kallisto \\
        index \\
        $args \\
        -i kallisto \\
        $fasta

    cat <<-END_VERSIONS > versions.yml
    "${task.process}":
        kallisto: \$(echo \$(kallisto 2>&1) | sed 's/^kallisto //; s/Usage.*\$//')
    END_VERSIONS
    """

    stub:
    """
    touch kallisto

    cat <<-END_VERSIONS > versions.yml
    "${task.process}":
        kallisto: \$(echo \$(kallisto 2>&1) | sed 's/^kallisto //; s/Usage.*\$//')
    END_VERSIONS
    """
}
