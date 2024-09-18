class UTILS {
    // Remove Nextflow version from pipeline_software_mqc_versions.yml
    public static Object removeNextflowVersion(pipeline_software_mqc_versions) {
        def softwareVersions = path(pipeline_software_mqc_versions).yaml
        if (softwareVersions.containsKey("Workflow")) softwareVersions.Workflow.remove("Nextflow")
        return softwareVersions
    }

    // Recursively list all files in a directory and its sub-directories, matching or not matching supplied suffixes
    public static getAllFilesFromDir(dir, List<String> includeRegexes = null, List<String> excludeRegexes = null) {
        def output = []
        new File(dir).eachFileRecurse() { file ->
            boolean matchesInclusion = includeRegexes ? includeRegexes.any { regex -> file.name.toString() ==~ regex } : true
            boolean matchesExclusion = excludeRegexes ? excludeRegexes.any { regex -> file.name.toString() ==~ regex } : false

            // Add files to the list if they match the includeRegexes and not the excludeRegexes
            if (matchesInclusion && !matchesExclusion) output.add(file)
        }
        return output.sort { it.path }
    }
    // Static (global) exclusion regexes list
    static List<String> exclusionRegexesForUnstableFileNames = [/.*\d{4}-\d{2}-\d{2}_\d{2}-\d{2}-\d{2}.*/]

    // These are the extension of all files to try to snapshot
    static List<String> snapshottablePatterns = [/.*\.(ctab|fasta|gtf|gz|html|json|log|pdf|png|svg|tab|tsv|txt|yml)$/]

    // These are the files to exclude when we want to snapshot
    static List<String> exclusionRegexesForUnstableFileContents = [
        // To exclude files with timestamps in the format YYYY-MM-DD_HH-MM-SS
        /.*\d{4}-\d{2}-\d{2}_\d{2}-\d{2}-\d{2}.*/,

        // To exlude the pipeline_software_mqc_versions.yml file that contains the Nextflow version
        /nf_core_rnaseq_software_mqc_versions\.yml/,

        // To exlude bbsplit files
        /.*\.stats\.txt/,

        // To exclude FASTQC reports
        /.*_raw\.html/,
        /.*_fastqc\.html/,

        // To exclude from the MultiQC reports
        /cutadapt_filtered_reads_plot-.*/,
        /cutadapt_trimmed_sequences_.*\.pdf/,
        /cutadapt_trimmed_sequences_.*\.svg/,
        /dupradar-section-plot\.pdf/,
        /dupradar-section-plot\.svg/,
        /fail_mapped_samples_table.*/,
        /fail_strand_check_table.*/,
        /fastqc-status-check-.*\.pdf/,
        /fastqc-status-check-.*\.svg/,
        /fastqc_adapter_content_plot\.pdf/,
        /fastqc_adapter_content_plot\.png/,
        /fastqc_adapter_content_plot\.svg/,
        /fastqc_overrepresented_sequences_plot-1\.pdf/,
        /fastqc_overrepresented_sequences_plot-1\.svg/,
        /fastqc_overrepresented_sequences_plot\.pdf/,
        /fastqc_overrepresented_sequences_plot\.svg/,
        /fastqc_per_base_n_content_plot-1\.pdf/,
        /fastqc_per_base_n_content_plot-1\.png/,
        /fastqc_per_base_n_content_plot-1\.svg/,
        /fastqc_per_base_n_content_plot\.pdf/,
        /fastqc_per_base_n_content_plot\.png/,
        /fastqc_per_base_n_content_plot\.svg/,
        /fastqc_per_base_sequence_quality_plot-1\.pdf/,
        /fastqc_per_base_sequence_quality_plot-1\.png/,
        /fastqc_per_base_sequence_quality_plot-1\.svg/,
        /fastqc_per_base_sequence_quality_plot\.pdf/,
        /fastqc_per_base_sequence_quality_plot\.png/,
        /fastqc_per_base_sequence_quality_plot\.svg/,
        /fastqc_per_sequence_gc_content_plot-1_Counts\.pdf/,
        /fastqc_per_sequence_gc_content_plot-1_Counts\.svg/,
        /fastqc_per_sequence_gc_content_plot-1_Percentages\.pdf/,
        /fastqc_per_sequence_gc_content_plot-1_Percentages\.svg/,
        /fastqc_per_sequence_gc_content_plot_Counts\.pdf/,
        /fastqc_per_sequence_gc_content_plot_Counts\.svg/,
        /fastqc_per_sequence_gc_content_plot_Percentages\.pdf/,
        /fastqc_per_sequence_gc_content_plot_Percentages\.svg/,
        /fastqc_per_sequence_quality_scores_plot-1\.pdf/,
        /fastqc_per_sequence_quality_scores_plot-1\.svg/,
        /fastqc_per_sequence_quality_scores_plot\.pdf/,
        /fastqc_per_sequence_quality_scores_plot\.svg/,
        /fastqc_sequence_counts_plot-1-cnt\.pdf/,
        /fastqc_sequence_counts_plot-1-cnt\.svg/,
        /fastqc_sequence_counts_plot-1-pct\.pdf/,
        /fastqc_sequence_counts_plot-1-pct\.svg/,
        /fastqc_sequence_counts_plot-cnt\.pdf/,
        /fastqc_sequence_counts_plot-cnt\.svg/,
        /fastqc_sequence_counts_plot-pct\.pdf/,
        /fastqc_sequence_counts_plot-pct\.svg/,
        /fastqc_sequence_duplication_levels_plot-1\.pdf/,
        /fastqc_sequence_duplication_levels_plot-1\.svg/,
        /fastqc_sequence_duplication_levels_plot\.pdf/,
        /fastqc_sequence_duplication_levels_plot\.svg/,
        /fastqc_sequence_length_distribution_plot\.pdf/,
        /fastqc_sequence_length_distribution_plot\.png/,
        /fastqc_sequence_length_distribution_plot\.svg/,
        /fastqc_top_overrepresented_sequences_table-1\.pdf/,
        /fastqc_top_overrepresented_sequences_table-1\.png/,
        /fastqc_top_overrepresented_sequences_table-1\.svg/,
        /fastqc_top_overrepresented_sequences_table-1\.txt/,
        /fastqc_top_overrepresented_sequences_table\.pdf/,
        /fastqc_top_overrepresented_sequences_table\.png/,
        /fastqc_top_overrepresented_sequences_table\.svg/,
        /fastqc_top_overrepresented_sequences_table\.txt/,
        /featurecounts_biotype_plot-cnt\.pdf/,
        /featurecounts_biotype_plot-cnt\.svg/,
        /featurecounts_biotype_plot-pct\.pdf/,
        /featurecounts_biotype_plot-pct\.svg/,
        /general_stats_table\.pdf/,
        /general_stats_table\.png/,
        /general_stats_table\.svg/,
        /hisat2_(pe|se)_plot-(cnt|pct)\.(pdf|png|svg)/,
        /hisat2_pe_plot\.txt/,
        /hisat2_se_plot.*\.(png|svg)/,
        /junction_saturation_known\.txt/,
        /junction_saturation_novel\.txt/,
        /kallisto_alignment.*.pdf/,
        /kallisto_alignment.*.png/,
        /kallisto_alignment.*.svg/,
        /kallisto_alignment\.txt/,
        /multiqc_data\.json/,
        /multiqc_dupradar-section-plot\.txt/,
        /multiqc_fail_strand_check_table\.txt/,
        /multiqc_general_stats\.txt/,
        /multiqc_hisat2\.txt/,
        /multiqc_kallisto\.txt/,
        /multiqc_picard_dups\.txt/,
        /multiqc_report\.html/,
        /multiqc_rsem\.txt/,
        /multiqc_rseqc_bam_stat\.txt/,
        /multiqc_rseqc_infer_experiment\.txt/,
        /multiqc_rseqc_junction_annotation\.txt/,
        /multiqc_rseqc_read_distribution\.txt/,
        /multiqc_salmon\.txt/,
        /(star_rsem|multiqc_salmon|multiqc_star_salmon)_deseq2_clustering-plot?(.*)\.(pdf|png|txt)/,
        /multiqc_salmon_deseq2_pca-plot.*\.txt/,
        /multiqc_samtools_flagstat\.txt/,
        /multiqc_samtools_stats\.txt/,
        /multiqc_software_versions\.txt/,
        /multiqc_sortmerna\.txt/,
        /multiqc_sources\.txt/,
        /multiqc_star\.txt/,
        /multiqc_star_salmon_deseq2_pca-plot\.txt/,
        /picard_deduplication-cnt\.pdf/,
        /picard_deduplication-cnt\.png/,
        /picard_deduplication-cnt\.svg/,
        /picard_deduplication-pct\.pdf/,
        /picard_deduplication-pct\.png/,
        /picard_deduplication-pct\.svg/,
        /picard_deduplication\.txt/,
        /qualimap_gene_coverage_profile_Counts\.pdf/,
        /qualimap_gene_coverage_profile_Counts\.svg/,
        /qualimap_gene_coverage_profile_Normalised\.pdf/,
        /qualimap_gene_coverage_profile_Normalised\.svg/,
        /qualimap_genomic_origin-cnt\.pdf/,
        /qualimap_genomic_origin-cnt\.png/,
        /qualimap_genomic_origin-cnt\.svg/,
        /qualimap_genomic_origin-pct\.pdf/,
        /qualimap_genomic_origin-pct\.png/,
        /qualimap_genomic_origin-pct\.svg/,
        /qualimap_genomic_origin\.txt/,
        /qualimap_rnaseq_genome_results\.txt/,
        /rsem_assignment_plot\.txt/,
        /rsem_assignment_plot-(cnt|pct)\.(pdf|png|svg)/,
        /rsem_multimapping_rates\.(pdf|svg|txt)/,
        /rseqc_bam_stat\.pdf/,
        /rseqc_bam_stat\.png/,
        /rseqc_bam_stat\.svg/,
        /rseqc_bam_stat\.txt/,
        /rseqc_infer_experiment_plot\.pdf/,
        /rseqc_infer_experiment_plot\.svg/,
        /rseqc_inner_distance\.txt/,
        /rseqc_inner_distance_plot_Counts\.pdf/,
        /rseqc_inner_distance_plot_Counts\.png/,
        /rseqc_inner_distance_plot_Counts\.svg/,
        /rseqc_inner_distance_plot_Counts\.txt/,
        /rseqc_inner_distance_plot_Percentages\.pdf/,
        /rseqc_inner_distance_plot_Percentages\.png/,
        /rseqc_inner_distance_plot_Percentages\.svg/,
        /rseqc_inner_distance_plot_Percentages\.txt/,
        /rseqc_junction_annotation_junctions_plot_Events-cnt\.pdf/,
        /rseqc_junction_annotation_junctions_plot_Events-cnt\.png/,
        /rseqc_junction_annotation_junctions_plot_Events-cnt\.svg/,
        /rseqc_junction_annotation_junctions_plot_Events-pct\.pdf/,
        /rseqc_junction_annotation_junctions_plot_Events-pct\.png/,
        /rseqc_junction_annotation_junctions_plot_Events-pct\.svg/,
        /rseqc_junction_annotation_junctions_plot_Events\.txt/,
        /rseqc_junction_annotation_junctions_plot_Junctions-cnt\.pdf/,
        /rseqc_junction_annotation_junctions_plot_Junctions-cnt\.png/,
        /rseqc_junction_annotation_junctions_plot_Junctions-cnt\.svg/,
        /rseqc_junction_annotation_junctions_plot_Junctions-pct\.pdf/,
        /rseqc_junction_annotation_junctions_plot_Junctions-pct\.png/,
        /rseqc_junction_annotation_junctions_plot_Junctions-pct\.svg/,
        /rseqc_junction_annotation_junctions_plot_Junctions\.txt/,
        /rseqc_junction_saturation_all\.txt/,
        /rseqc_junction_saturation_plot_All_Junctions\.pdf/,
        /rseqc_junction_saturation_plot_All_Junctions\.png/,
        /rseqc_junction_saturation_plot_All_Junctions\.svg/,
        /rseqc_junction_saturation_plot_All_Junctions\.txt/,
        /rseqc_junction_saturation_plot_Known_Junctions\.pdf/,
        /rseqc_junction_saturation_plot_Known_Junctions\.png/,
        /rseqc_junction_saturation_plot_Known_Junctions\.svg/,
        /rseqc_junction_saturation_plot_Known_Junctions\.txt/,
        /rseqc_junction_saturation_plot_Novel_Junctions\.pdf/,
        /rseqc_junction_saturation_plot_Novel_Junctions\.png/,
        /rseqc_junction_saturation_plot_Novel_Junctions\.svg/,
        /rseqc_junction_saturation_plot_Novel_Junctions\.txt/,
        /rseqc_read_distribution_plot-cnt\.pdf/,
        /rseqc_read_distribution_plot-cnt\.png/,
        /rseqc_read_distribution_plot-cnt\.svg/,
        /rseqc_read_distribution_plot-pct\.pdf/,
        /rseqc_read_distribution_plot-pct\.png/,
        /rseqc_read_distribution_plot-pct\.svg/,
        /rseqc_read_distribution_plot\.txt/,
        /rseqc_read_dups\.txt/,
        /rseqc_read_dups_plot\.pdf/,
        /rseqc_read_dups_plot\.svg/,
        /rseqc_read_dups_plot\.txt/,
        /(salmon|star_rsem)_deseq2_(clustering|pca)-plot\.pdf/,
        /(salmon|star_rsem)_deseq2_(clustering|pca)-plot\.png/,
        /(salmon|star_rsem)_deseq2_(clustering|pca)-plot\.svg/,
        /(salmon|star_rsem)_deseq2_(pca|pca)-plot\.pdf/,
        /(salmon|star_rsem)_deseq2_(pca|pca)-plot\.png/,
        /(salmon|star_rsem)_deseq2_(pca|pca)-plot\.svg/,
        /salmon_plot\.pdf/,
        /salmon_plot\.png/,
        /salmon_plot\.svg/,
        /salmon_plot\.txt/,
        /samtools-flagstat-dp_Percentage_of_total\.pdf/,
        /samtools-flagstat-dp_Percentage_of_total\.png/,
        /samtools-flagstat-dp_Percentage_of_total\.svg/,
        /samtools-flagstat-dp_Percentage_of_total\.txt/,
        /samtools-flagstat-dp_Read_counts\.pdf/,
        /samtools-flagstat-dp_Read_counts\.png/,
        /samtools-flagstat-dp_Read_counts\.svg/,
        /samtools-flagstat-dp_Read_counts\.txt/,
        /samtools-idxstats-mapped-reads-plot_Normalised_Counts-cnt\.pdf/,
        /samtools-idxstats-mapped-reads-plot_Normalised_Counts-cnt\.svg/,
        /samtools-idxstats-mapped-reads-plot_Normalised_Counts-log\.pdf/,
        /samtools-idxstats-mapped-reads-plot_Normalised_Counts-log\.svg/,
        /samtools-idxstats-mapped-reads-plot_Observed_over_Expected_Counts-cnt\.pdf/,
        /samtools-idxstats-mapped-reads-plot_Observed_over_Expected_Counts-cnt\.svg/,
        /samtools-idxstats-mapped-reads-plot_Observed_over_Expected_Counts-log\.pdf/,
        /samtools-idxstats-mapped-reads-plot_Observed_over_Expected_Counts-log\.svg/,
        /samtools-idxstats-mapped-reads-plot_Raw_Counts-cnt\.pdf/,
        /samtools-idxstats-mapped-reads-plot_Raw_Counts-cnt\.svg/,
        /samtools-idxstats-mapped-reads-plot_Raw_Counts-log\.pdf/,
        /samtools-idxstats-mapped-reads-plot_Raw_Counts-log\.svg/,
        /samtools-stats-dp\.pdf/,
        /samtools-stats-dp\.png/,
        /samtools-stats-dp\.svg/,
        /samtools-stats-dp\.txt/,
        /samtools_alignment_plot-cnt\.pdf/,
        /samtools_alignment_plot-cnt\.png/,
        /samtools_alignment_plot-cnt\.svg/,
        /samtools_alignment_plot-pct\.pdf/,
        /samtools_alignment_plot-pct\.png/,
        /samtools_alignment_plot-pct\.svg/,
        /samtools_alignment_plot\.txt/,
        /sortmerna-detailed-plot-(cnt|pct)\.(pdf|svg)/,
        /sortmerna-detailed-plot\.txt/,
        /star_alignment_plot-cnt\.pdf/,
        /star_alignment_plot-cnt\.png/,
        /star_alignment_plot-cnt\.svg/,
        /star_alignment_plot-pct\.pdf/,
        /star_alignment_plot-pct\.png/,
        /star_alignment_plot-pct\.svg/,
        /star_alignment_plot\.txt/,
        /(star_rsem|star_salmon)_deseq2_(clustering|pca)-plot?(.*)\.(pdf|png|svg)/,
        /star_salmon_deseq2_pca-plot\.(pdf|png|svg)/,
        /star_summary_table\.(pdf|png|svg|txt)/,

        // To exclude from deseq2_qc
        /RAP1_IAA_30M_REP1\.txt/,
        /RAP1_UNINDUCED_REP1\.txt/,
        /RAP1_UNINDUCED_REP2\.txt/,
        /WT_REP1\.txt/,
        /WT_REP2\.txt/,
        /deseq2\.dds\.RData/,
        /deseq2\.pca\.vals\.txt/,
        /deseq2\.plots\.pdf/,
        /deseq2\.sample\.dists\.txt/,
        /deseq2\.size_factors\.RData/,

        // To exclude from kallisto
        /abundance\.tsv/,
        /kallisto_quant\.log/,
        /kallisto\.merged\.gene_counts\.tsv/,
        /kallisto\.merged\.gene_counts_length_scaled\.tsv/,
        /kallisto\.merged\.gene_counts_scaled\.tsv/,
        /kallisto\.merged\.gene_lengths\.tsv/,
        /kallisto\.merged\.gene_tpm\.tsv/,
        /kallisto\.merged\.transcript_counts\.tsv/,
        /kallisto\.merged\.transcript_lengths\.tsv/,
        /kallisto\.merged\.transcript_tpm\.tsv/,
        /run_info\.json/,

        // To exclude from salmon quant
        /fld\.gz/,
        /meta_info\.json/,
        /flenDist\.txt/,
        /salmon_quant\.log/,
        /quant\.genes\.sf/,
        /quant\.sf/,

        // To exclude from salmon
        /salmon\.merged\.gene_counts\.SummarizedExperiment\.rds/,
        /salmon\.merged\.gene_counts\.tsv/,
        /salmon\.merged\.gene_counts_length_scaled\.SummarizedExperiment\.rds/,
        /salmon\.merged\.gene_counts_length_scaled\.tsv/,
        /salmon\.merged\.gene_counts_scaled\.SummarizedExperiment\.rds/,
        /salmon\.merged\.gene_counts_scaled\.tsv/,
        /salmon\.merged\.gene_lengths\.tsv/,
        /salmon\.merged\.gene_tpm\.tsv/,
        /salmon\.merged\.transcript_counts\.SummarizedExperiment\.rds/,
        /salmon\.merged\.transcript_counts\.tsv/,
        /salmon\.merged\.transcript_lengths\.tsv/,
        /salmon\.merged\.transcript_tpm\.tsv/,

        // To exclude bigwig
        /.*\.(forward|reverse)\.bigWig/,

        // To exlude dupradar
        /.*_duprateExpBoxplot\.pdf/,
        /.*_expressionHist\.pdf/,
        /.*_duprateExpDens\.pdf/,

        // To exclude featurecounts
        /.*\.featureCounts\.txt\.summary/,

        // To exclude star salmon
        /.*\.Log\.?(final|progress)\.out/,

        // To exclude Picard Markduplicates metrics
        /.*\.markdup\.sorted\.MarkDuplicates\.metrics\.txt/,

        // To exclude Qualimap files
        /Junction\sAnalysis\.png/,
        /Reads\sGenomic\sOrigin\.png/,
        /qualimapReport\.html/,
        /rnaseq_qc_results\.txt/,

        // To exclude rseqc
        /.*\.DupRate_plot\.(pdf|r)/,
        /.*\.inner_distance.*/,
        /.*\.junction.*/,
        /.*\.(pos|seq)\.DupRate\.xls/,
        /.*\.read_distribution\.txt/,
        /.*\.splice_(events|junction)\.pdf/,
        /.*\.bam_stat\.txt/,

        // To exclude from samtools stats
        /.*\.sorted\.bam\.(flagstat|idxstats|stats)/,

        // To exclude from hisat2
        /.*hisat2\.summary/,

        // To exclude from stringtie
        /t_data\.ctab/,
        /.*\.coverage\.gtf/,
        /.*\.gene\.abundance\.txt/,
        /.*\.transcripts\.gtf/,

        // To exclude from sortmerna
        /.*\.sortmerna\.log/,

        // To exclude log from star rsem
        /RAP1_IAA_30M_REP1\.log/,
        /RAP1_UNINDUCED_REP1\.log/,
        /RAP1_UNINDUCED_REP2\.log/,
        /WT_REP1\.log/,
        /WT_REP2\.log/,

        // To exclude markdup
        /.*\.markdup\.sorted\.bam?(\.bai)/,

        // To exclude trimgalore
        /.*\.fastq\.gz_trimming_report\.txt/
    ]

}
