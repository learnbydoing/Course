import sys
import math

data = [[2,0,4,3,0,1,0,2], [0,2,4,0,2,3,0,0], [4,0,1,3,0,1,0,1], [0,1,0,2,0,0,1,0], [0,0,2,0,0,4,0,0], [1,1,0,2,0,1,1,3], [2,1,3,4,0,2,0,2]]

def euclidean(matrix):
	len_doc = len(matrix)
	len_term = len(matrix[0])
	result = [[0.0 for x in range(len_term)] for x in range(len_term)]
	for i in range(len_term):
		for j in range(i + 1, len_term):
			val = 0.0
			for k in range(len_doc):
				val += float((matrix[k][i] - matrix[k][j])**2)
			result[i][j] = val **(0.5)
			result[j][i] = result[i][j]
			
	# euclidean
	print '  \t' + '\t'.join([str('T{0}'.format(i+1)) for i in range(len_term)])
	for i in range(len_term):
		print 'T{0}\t'.format(i + 1) + '\t'.join(str(format(x, '.2f')) for x in result[i])

	print ''

	# threshold of 4.0
	print '  \t' + '\t'.join([str('T{0}'.format(i+1)) for i in range(len_term)])
	for i in range(len_term):
		print 'T{0}\t'.format(i + 1) + '\t'.join(str(format(x <= 4.0, 'd')) for x in result[i])

euclidean(data)