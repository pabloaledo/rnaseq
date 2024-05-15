process GFFREAD {
    tag "$gff"
    label 'process_low'

    conda "gffread=0.12.1"

    input:
    path gff

    output:
    path "*.gtf"        , emit: gtf
    path "versions.yml" , emit: versions

    when:
    task.ext.when == null || task.ext.when

    script:
    def args   = task.ext.args   ?: ''
    def prefix = task.ext.prefix ?: "${gff.baseName}"
    """
    gffread \\
        $gff \\
        $args \\
        -o ${prefix}.gtf
    cat <<-END_VERSIONS > versions.yml
    "${task.process}":
        gffread: \$(gffread --version 2>&1)
    END_VERSIONS
    """
}
